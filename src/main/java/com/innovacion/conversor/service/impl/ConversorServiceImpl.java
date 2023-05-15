package com.innovacion.conversor.service.impl;

import com.innovacion.conversor.dto.ConvertRequest;
import com.innovacion.conversor.service.IConversorService;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import static com.innovacion.conversor.util.PythonExecutorUtil.executePythonFunction;

@Service
@ConditionalOnProperty(prefix = "app", name = "conversor-pdf", havingValue = "python")
public class ConversorServiceImpl implements IConversorService {

    @Override
    public PyObject convert(ConvertRequest request) {
        PythonInterpreter interpreter = new PythonInterpreter();


        String pythonCode = "import fitz as f\n" +
                "import re\n" +
                "import pandas as pd\n" +
                "import math\n" +
                "import io\n" +
                "\n" +

                "def convert(request):\n" +
                "    excel_file = request.files[\"excelFile\"]\n" +
                "    pdf_file = request.files[\"pdfFile\"]\n" +
                "    ganancia = float(request.form[\"ganancia\"])\n" +
                "    new_pdf_name = request.form[\"newPdfName\"]\n" +
                "\n" +
                "    print(f\"Archivo de Excel recibido: {excel_file.filename}\")\n" +
                "    print(f\"Archivo PDF recibido: {pdf_file.filename}\")\n" +
                "    print(f\"Ganancia recibida: {ganancia}\")\n" +
                "    print(f\"Nuevo nombre del archivo PDF: {new_pdf_name}\")\n" +
                "\n" +
                "    df_precios = pd.read_excel(excel_file)\n" +
                "    df_precios = df_precios.rename(columns={'Unnamed: 0': 'Código',\n" +
                "                                            'LOS PRECIOS NO INCLUYEN IVA, PRECIOS SUJETOS A MODIFICACIONES SIN PREVIO AVISO.': 'Precios'})\n" +
                "    df_codigo = df_precios[['Código', 'Precios']]\n" +
                "\n" +
                "    df_codigo = df_codigo.dropna(subset=['Código', 'Precios'])\n" +
                "    df_codigo = df_codigo[df_codigo['Código'].str.contains('\\d')]\n" +
                "    df_codigo['Precios con ganancia'] = df_codigo['Precios'].apply(\n" +
                "        lambda x: math.ceil(x * (1 + ganancia / 100) / 50) * 50)\n" +
                "\n" +
                "    pdf_data = pdf_file.read()\n" +
                "    pdf_buffer = io.BytesIO(pdf_data)\n" +
                "    documento = f.open(\"pdf\", pdf_buffer)\n" +
                "\n" +
                "    regex = r\"\\d{2,}/\\d{2,}|/\\d{2,}\\b|\\d{2,}/\\b\"\n" +
                "    for numeroDePagina in range(len(documento)):\n" +
                "        pagina = documento.load_page(numeroDePagina)\n" +
                "        text = pagina.get_text(\"text\")\n" +
                "        codigos = re.findall(regex, text)\n" +
                "        for codigo in codigos:\n" +
                "            precio_serie = df_codigo.loc[df_codigo['Código'] == codigo, 'Precios con ganancia']\n" +
                "            if not precio_serie.empty:\n" +
                "                precio = precio_serie.iloc[0]\n" +
                "                text_instances = pagina.search_for(codigo)\n" +
                "                for inst in text_instances:\n" +
                "                    bbox = inst.irect\n" +
                "                    new_y = bbox.y0 - 70\n" +
                "                    new_x = bbox.x0\n" +
                "                    pagina.insert_text((new_x, new_y), str(precio), fontsize=20, fill=(1, 0.3, 0), render_mode=2)\n" +
                "\n" +
                "    new_pdf_buffer = io.BytesIO()\n" +
                "    documento.save(new_pdf_buffer)\n" +
                "    new_pdf_buffer.seek(0)\n" +
                "\n" +
                "    print(\"Generando archivo PDF nuevo...\")\n" +
                "\n" +
                "    return new_pdf_buffer\n" +
                "\n" +
                "request = {}\n" +
                "request['files'] = {'excelFile': 'archivo_excel.xlsx', 'pdfFile': 'archivo_pdf.pdf'}\n" +
                "request['form'] = {'ganancia': '0.5', 'newPdfName': 'nuevo_pdf.pdf'}\n" +
                "\n" +
                "resultado = convert(request)\n" +
                "print(resultado)\n";

        // Ejecutar el código Python
        interpreter.exec(pythonCode);

        PyObject response =executePythonFunction("convert", request);

        //byte[] bytes = (byte[]) requestAPy.__tojava__(byte[].class);

        return response;
    }
}
