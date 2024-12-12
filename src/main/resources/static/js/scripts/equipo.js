const url = "/v1/equipos";

// Función para hacer la solicitud AJAX
function ajaxRequest(type, endpoint, data = null) {
    return $.ajax({
        type,
        url: endpoint,
        data: data ? JSON.stringify(data) : null,
        dataType: "json",
        contentType: data ? "application/json" : undefined,
        cache: false,
        timeout: 600000,
    });
}

// Función para guardar o actualizar un equipo
function save(bandera) {
    const id = $("#guardar").data("id");
    const equipo = {
        id,
        nombre: $("#nombre").val(),
        ciudad: $("#ciudad").val(),
        fechaCreacion: $("#fechaCreacion").val(),
        numeroJugadores: $("#numeroJugadores").val()
    };

    const type = bandera === 1 ? "POST" : "PUT";
    const endpoint = bandera === 1 ? url : `${url}/${id}`;

    ajaxRequest(type, endpoint, equipo)
        .done((data) => {
            if (data.ok) {
                $("#modal-update").modal("hide");
                getTabla();
                $("#error-message").addClass("d-none");
                Swal.fire({
                    icon: 'success',
                    title: `Se ha ${bandera === 1 ? 'guardado' : 'actualizado'} el equipo`,
                    showConfirmButton: false,
                    timer: 1500
                });
                clear();
            } else {
                showError(data.message);
            }
        }).fail(function (jqXHR) {
            let errorMessage = jqXHR.responseJSON && jqXHR.responseJSON.message ? jqXHR.responseJSON.message : "Error inesperado. Código: " + jqXHR.status;
            showError(errorMessage);
        });
}

// Función para mostrar un mensaje de error
function showError(message) {
    $("#error-message").text(message).removeClass("d-none");
}

// Función para eliminar un equipo
function deleteFila(id) {
    ajaxRequest("DELETE", `${url}/${id}`)
        .done((data) => {
            if (data.ok) {
                Swal.fire({
                    icon: 'success',
                    title: 'Se ha eliminado el equipo',
                    showConfirmButton: false,
                    timer: 1500
                });
                getTabla();
            } else {
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: data.message,
                    confirmButtonText: 'Aceptar'
                });
            }
        })
        .fail(handleError);
}

// Función para obtener la lista de equipos y actualizar la tabla
function getTabla() {
    ajaxRequest("GET", url)
        .done((data) => {
            const t = $("#tablaRegistros").DataTable();
            t.clear().draw(false);

            if (data.ok) {
                $.each(data.body, (index, equipo) => {
                    const botonera = `
                        <button type="button" class="btn btn-warning btn-xs editar">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button type="button" class="btn btn-danger btn-xs eliminar">
                            <i class="fas fa-trash"></i>
                        </button>`;

                    t.row.add([botonera, equipo.id, equipo.nombre, equipo.ciudad, equipo.fechaCreacion, equipo.numeroJugadores]);
                });
                t.draw(); 
            } else {
                console.error("Error en la respuesta: ", data.message);
            }
        })
        .fail(handleError);
}

// Función para obtener los datos de un equipo y mostrarlos en el modal
function getFila(id) {
    ajaxRequest("GET", `${url}/${id}`)
        .done((data) => {
            if (data.ok) {
                $("#modal-title").text("Editar equipo");
                $("#nombre").val(data.body.nombre);
                $("#ciudad").val(data.body.ciudad);
                $("#fechaCreacion").val(data.body.fechaCreacion);
                $("#numeroJugadores").val(data.body.numeroJugadores);
                $("#guardar").data("id", data.body.id).data("bandera", 0);
                $("#modal-update").modal("show");
            } else {
                showError(data.message);
            }
        })
        .fail(handleError);
}

// Función para limpiar los campos del modal
function clear() {
    $("#modal-title").text("Nuevo equipo");
    $("#nombre").val("");
    $("#ciudad").val("");
    $("#fechaCreacion").val("");
    $("#numeroJugadores").val("");
    $("#guardar").data("id", 0).data("bandera", 1);
}

// Función para manejar errores
function handleError(jqXHR) {
    const errorMessage = jqXHR.responseJSON?.message || `Error inesperado. Código: ${jqXHR.status}`;
    Swal.fire({
        icon: 'error',
        title: 'Error',
        text: errorMessage,
        confirmButtonText: 'Aceptar'
    });
}

// Document Ready
$(document).ready(function () {
    $("#tablaRegistros").DataTable({
        language: {
            lengthMenu: "Mostrar _MENU_ equipos",
            zeroRecords: "No se encontraron coincidencias",
            info: "Mostrando del _START_ al _END_ de _TOTAL_ equipos",
            infoEmpty: "Sin resultados",
            search: "Buscar: ",
            paginate: {
                first: "Primero",
                last: "Último",
                next: "Siguiente",
                previous: "Anterior",
            },
        },
        columnDefs: [
            { targets: 0, orderable: false }
        ],
    });

    clear();

    // Abrir el modal de "Nuevo Equipo"
    $("#nuevo").click(clear);

    // Guardar o actualizar equipo
    $("#guardar").click(() => save($("#guardar").data("bandera")));

    // Eliminar equipo
    $(document).on('click', '.eliminar', function () {
        const id = $(this).closest('tr').find('td:eq(1)').text();
        Swal.fire({
            title: 'Eliminar equipo',
            text: "¿Está seguro de querer eliminar este equipo?",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Si'
        }).then((result) => {
            if (result.isConfirmed) {
                deleteFila(id);
            }
        });
    });

    // Editar equipo
    $(document).on('click', '.editar', function () {
        const id = $(this).closest('tr').find('td:eq(1)').text();
        getFila(id);
    });

    // Obtener la lista de equipos al cargar la página
    getTabla();
});
