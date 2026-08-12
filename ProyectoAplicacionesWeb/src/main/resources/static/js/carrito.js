function addToCart(idProducto) {

    /*
     * Obtenemos el token CSRF generado
     * por Spring Security.
     */
    const csrfToken =
            document
                .querySelector("meta[name='_csrf']")
                ?.getAttribute('content');

    const csrfHeader =
            document
                .querySelector("meta[name='_csrf_header']")
                ?.getAttribute('content');


    /*
     * Preparamos los datos que enviaremos
     * al controlador.
     */
    const body =
            new URLSearchParams();

    body.append(
        'idProducto',
        idProducto
    );


    /*
     * Encabezados de la petición.
     */
    const headers = {

        'Content-Type':
            'application/x-www-form-urlencoded;charset=UTF-8'

    };


    /*
     * Agregamos CSRF solamente si existe.
     */
    if (csrfToken && csrfHeader) {

        headers[csrfHeader] =
                csrfToken;

    }


    /*
     * Petición AJAX al controlador.
     */
    fetch(
        '/carrito/agregar',
        {

            method: 'POST',

            headers: headers,

            body: body.toString()

        }
    )

    /*
     * Convertimos la respuesta del servidor
     * a HTML.
     */
    .then(async response => {

        const html =
                await response.text();

        if (!response.ok) {

            throw new Error(

                html
                ||
                'No fue posible agregar el producto.'

            );

        }

        return html;

    })


    /*
     * Reemplazamos solamente el fragmento
     * correspondiente al carrito.
     */
    .then(html => {

        document
            .getElementById('resultBlock')
            .outerHTML = html;

    })


    /*
     * Si ocurre algún error.
     */
    .catch(error => {

        alert(

            'Error al agregar producto al carrito: '
            +
            error.message

        );

    });

}