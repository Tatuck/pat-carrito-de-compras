document.addEventListener('DOMContentLoaded', () => {
  const params = new URLSearchParams(window.location.search);
  const id = parseInt(params.get('id'));
  const producto = PRODUCTOS.find(p => p.id === id);

  if (!producto) {
    window.location.href = 'productos.html';
    return;
  }

  document.title = `${producto.nombre} | Plata's Coffee`;
  document.getElementById('prod-imagen').src = `./images/${producto.imagen}`;
  document.getElementById('prod-imagen').alt = producto.nombre;
  document.getElementById('prod-nombre').textContent = producto.nombre;
  document.getElementById('prod-precio').textContent = `${producto.precio.toFixed(2)}€`;
  document.getElementById('prod-descripcion').textContent =
    `${producto.nombre} es un café de especialidad con notas de ${producto.notas.toLowerCase()} ` +
    `Originario de ${producto.origen}, cultivado a ${producto.altitud}.`;
  document.getElementById('prod-origen').textContent = producto.origen;
  document.getElementById('prod-altitud').textContent = producto.altitud;
  document.getElementById('prod-proceso').textContent = producto.proceso;
  document.getElementById('prod-tueste').textContent = producto.tueste;

  document.getElementById('form-carrito').addEventListener('submit', async (e) => {
    e.preventDefault();
    const cantidad = parseInt(document.getElementById('cantidad').value);
    const molienda = document.getElementById('molienda').value;
    const btn = e.submitter;
    btn.disabled = true;
    btn.textContent = 'Añadiendo...';

    const carritoId = await getOrCreateCarrito();
    if (!carritoId) {
      btn.disabled = false;
      btn.textContent = 'Añadir al Carrito';
      return;
    }

    const res = await fetch(`/api/carritos/${carritoId}/lineas`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        idArticulo: producto.id,
        precioUnitario: producto.precio,
        numUnidades: cantidad
      })
    });

    if (res.ok) {
      const linea = await res.json();
      saveMolienda(linea.idLineaCarrito, molienda);
      window.location.href = 'carrito.html';
    } else {
      alert('Error al añadir al carrito. Inténtalo de nuevo.');
      btn.disabled = false;
      btn.textContent = 'Añadir al Carrito';
    }
  });
});
