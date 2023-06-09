import fitz as f
import re
import pandas as pd
import math
import io


def convert(request):

    type(request)
    excel_file = request.files["excelFile"]
    pdf_file = request.files["pdfFile"]
    ganancia = float(request.form["ganancia"])
    new_pdf_name = request.form["newPdfName"]

    print(f"Archivo de Excel recibido: {excel_file.filename}")
    print(f"Archivo PDF recibido: {pdf_file.filename}")
    print(f"Ganancia recibida: {ganancia}")
    print(f"Nuevo nombre del archivo PDF: {new_pdf_name}")

    df_precios = pd.read_excel(excel_file)
    df_precios = df_precios.rename(columns={'Unnamed: 0': 'Código',
                                            'LOS PRECIOS NO INCLUYEN IVA, PRECIOS SUJETOS A MODIFICACIONES SIN PREVIO AVISO.': 'Precios'})
    df_codigo = df_precios[['Código', 'Precios']]

    df_codigo = df_codigo.dropna(subset=['Código', 'Precios'])
    df_codigo = df_codigo[df_codigo['Código'].str.contains('\d')]
    df_codigo['Precios con ganancia'] = df_codigo['Precios'].apply(
        lambda x: math.ceil(x * (1 + ganancia / 100) / 50) * 50)

    pdf_data = pdf_file.read()
    pdf_buffer = io.BytesIO(pdf_data)
    documento = f.open("pdf", pdf_buffer)

    regex = r"\d{2,}/\d{2,}|/\d{2,}\b|\d{2,}/\b"
    for numeroDePagina in range(len(documento)):
        pagina = documento.load_page(numeroDePagina)
        text = pagina.get_text("text")
        codigos = re.findall(regex, text)
        for codigo in codigos:
            precio_serie = df_codigo.loc[df_codigo['Código'] == codigo, 'Precios con ganancia']
            if not precio_serie.empty:
                precio = precio_serie.iloc[0]
                text_instances = pagina.search_for(codigo)
                for inst in text_instances:
                    bbox = inst.irect
                    new_y = bbox.y0 - 70
                    new_x = bbox.x0
                    pagina.insert_text((new_x, new_y), str(precio), fontsize=20, fill=(1, 0.3, 0), render_mode=2)

    new_pdf_buffer = io.BytesIO()
    documento.save(new_pdf_buffer)
    new_pdf_buffer.seek(0)

    print("Generando archivo PDF nuevo...")

    #return new_pdf_buffer.
    response = Response(new_pdf_buffer.getvalue(), mimetype="application/pdf")
    response.headers.set("Content-Disposition", "attachment", filename=new_pdf_name + ".pdf")
    return response

    return pdf_bytes

