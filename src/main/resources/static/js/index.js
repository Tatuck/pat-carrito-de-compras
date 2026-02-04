// Añadir carrito
document.getElementById('añadirButton').addEventListener('click', async () => {
    const idCarrito = parseInt(document.getElementById('idCarrito').value);
    const idArticulo = parseInt(document.getElementById('idArticulo').value);
    const descripcion = document.getElementById('descripcion').value;
    const unidades = parseInt(document.getElementById('unidades').value);

    if (!idCarrito || !idArticulo || !descripcion || !unidades) {
        alert('Por favor, completa todos los campos');
        return;
    }

    if (unidades < 1) {
        alert('Las unidades deben ser al menos 1');
        return;
    }

    const carrito = {
        idCarrito: idCarrito,
        idArticulo: idArticulo,
        descripcion: descripcion,
        unidades: unidades
    };

    try {
        const response = await fetch('/api/crear', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(carrito)
        });

        if (response.ok) {
            alert('Carrito añadido exitosamente');
            location.reload();
        } else {
            const error = await response.text();
            alert('Error al añadir el carrito: ' + error);
        }
    } catch (error) {
        alert('Error al conectar con el servidor: ' + error.message);
    }
});

// Modificar carrito
document.getElementById('modificarButton').addEventListener('click', async () => {
    const idCarrito = parseInt(document.getElementById('modificarIdCarrito').value);
    const idArticulo = document.getElementById('modificarIdArticulo').value;
    const descripcion = document.getElementById('modificarDescripcion').value;
    const unidades = document.getElementById('modificarUnidades').value;

    if (!idCarrito) {
        alert('Por favor, ingresa el ID del carrito a modificar');
        return;
    }

    // Crear objeto solo con los campos que tienen valor
    const actualizacion = {};
    if (idArticulo) actualizacion.idArticulo = parseInt(idArticulo);
    if (descripcion) actualizacion.descripcion = descripcion;
    if (unidades) {
        const unidadesNum = parseInt(unidades);
        if (unidadesNum < 1) {
            alert('Las unidades deben ser al menos 1');
            return;
        }
        actualizacion.unidades = unidadesNum;
    }

    if (Object.keys(actualizacion).length === 0) {
        alert('Por favor, debes poner al menos un campo para modificar');
        return;
    }

    try {
        const response = await fetch(`/api/modificar/${idCarrito}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(actualizacion)
        });

        if (response.ok) {
            alert('Carrito modificado exitosamente');
            location.reload();
        } else {
            const error = await response.text();
            alert('Error al modificar el carrito: ' + error);
        }
    } catch (error) {
        alert('Error al conectar con el servidor: ' + error.message);
    }
});

// Borrar carrito
async function eliminarCarrito(idCarrito) {
    if (confirm(`¿Estás seguro de que quieres eliminar el carrito con ID ${idCarrito}?`)) {
        try {
            const response = await fetch(`/api/eliminar/${idCarrito}`, {
                method: 'DELETE'
            });

            if (response.ok) {
                alert('Carrito eliminado!');
                location.reload();
            } else {
                const error = await response.text();
                alert('Error al eliminar el carrito: ' + error);
            }
        } catch (error) {
            alert('Error al conectar con el servidor: ' + error.message);
        }
    }
};
