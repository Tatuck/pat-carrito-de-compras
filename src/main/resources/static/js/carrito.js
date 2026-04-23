const ENVIO = 4.95;

async function loadCarrito() {
  const carritoId = localStorage.getItem('carritoId');
  const tbody = document.getElementById('lineas-tbody');
  const btnVaciar = document.getElementById('btn-vaciar');

  if (!carritoId) {
    renderCarritoVacio(tbody);
    btnVaciar.style.display = 'none';
    actualizarTotales(0);
    return;
  }

  try {
    const res = await fetch(`/api/carritos/${carritoId}`);
    if (!res.ok) {
      localStorage.removeItem('carritoId');
      renderCarritoVacio(tbody);
      btnVaciar.style.display = 'none';
      actualizarTotales(0);
      return;
    }

    const carrito = await res.json();
    const lineas = carrito.lineas || [];
    const moliendaMap = getMoliendaMap();

    tbody.innerHTML = '';

    if (lineas.length === 0) {
      renderCarritoVacio(tbody);
      btnVaciar.style.display = 'none';
      actualizarTotales(0);
      return;
    }

    lineas.forEach(linea => {
      const producto = PRODUCTOS.find(p => p.id === linea.idArticulo);
      const nombre = producto ? producto.nombre : `Artículo #${linea.idArticulo}`;
      const molienda = moliendaMap[linea.idLineaCarrito] || '—';
      const tr = document.createElement('tr');
      tr.innerHTML = `
        <td>${nombre}</td>
        <td>${formatMolienda(molienda)}</td>
        <td>
          <input type="number" class="input-cantidad" value="${linea.numUnidades}"
            min="1" max="10" data-id="${linea.idLineaCarrito}"
            data-precio="${linea.precioUnitario}" data-articulo="${linea.idArticulo}"
            style="width:60px; padding:4px; border:1px solid var(--color-border); border-radius:4px;">
        </td>
        <td>${linea.costeLinea.toFixed(2)}€</td>
        <td>
          <button class="btn-eliminar" data-id="${linea.idLineaCarrito}"
            style="background:none; border:none; cursor:pointer; color:#c0392b; font-size:1.1rem;" title="Eliminar">✕</button>
        </td>`;
      tbody.appendChild(tr);
    });

    tbody.querySelectorAll('.btn-eliminar').forEach(btn => {
      btn.addEventListener('click', () => eliminarLinea(parseInt(btn.dataset.id)));
    });

    tbody.querySelectorAll('.input-cantidad').forEach(input => {
      input.addEventListener('change', () => {
        const nuevaCantidad = parseInt(input.value);
        if (nuevaCantidad >= 1) actualizarCantidad(parseInt(input.dataset.id), nuevaCantidad, parseFloat(input.dataset.precio), parseInt(input.dataset.articulo));
      });
    });

    actualizarTotales(carrito.totalPrecio);
    btnVaciar.style.display = '';

  } catch (err) {
    tbody.innerHTML = '<tr><td colspan="5">Error cargando el carrito.</td></tr>';
  }
}

function renderCarritoVacio(tbody) {
  tbody.innerHTML = '<tr><td colspan="5" style="text-align:center; padding:30px; color:#888;">Tu carrito está vacío. <a href="productos.html">¡Añade algo!</a></td></tr>';
}

function actualizarTotales(subtotal) {
  const conEnvio = subtotal > 0 ? subtotal + ENVIO : 0;
  document.getElementById('subtotal').textContent = `${subtotal.toFixed(2)}€`;
  document.getElementById('envio').textContent = subtotal > 0 ? `${ENVIO.toFixed(2)}€` : '—';
  document.getElementById('total').textContent = `${conEnvio.toFixed(2)}€`;
}

async function eliminarLinea(idLinea) {
  const res = await fetch(`/api/lineas/${idLinea}`, { method: 'DELETE' });
  if (res.ok) {
    removeMolienda(idLinea);
    loadCarrito();
  } else {
    alert('Error al eliminar la línea.');
  }
}

async function actualizarCantidad(idLinea, numUnidades, precioUnitario, idArticulo) {
  const res = await fetch(`/api/lineas/${idLinea}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ idArticulo, precioUnitario, numUnidades })
  });
  if (res.ok) {
    loadCarrito();
  } else {
    alert('Error al actualizar la cantidad.');
  }
}

async function vaciarCarrito() {
  const carritoId = localStorage.getItem('carritoId');
  if (!carritoId) return;
  if (!confirm('¿Seguro que quieres vaciar el carrito?')) return;

  const res = await fetch(`/api/carritos/${carritoId}`, { method: 'DELETE' });
  if (res.ok) {
    localStorage.removeItem('carritoId');
    localStorage.removeItem('lineasMolienda');
    loadCarrito();
  } else {
    alert('Error al vaciar el carrito.');
  }
}

function formatMolienda(val) {
  const map = { grano: 'En Grano', espresso: 'Espresso', filtro: 'Filtro / V60', prensa: 'Prensa Francesa', italiana: 'Moka / Italiana' };
  return map[val] || val;
}

document.addEventListener('DOMContentLoaded', () => {
  if (!localStorage.getItem('carritoId')) {
    hacerLogin().then(() => loadCarrito());
  } else {
    loadCarrito();
  }

  document.getElementById('btn-vaciar').addEventListener('click', vaciarCarrito);

  document.getElementById('form-checkout').addEventListener('submit', async (e) => {
    e.preventDefault();
    const carritoId = localStorage.getItem('carritoId');
    if (!carritoId) return;

    const btn = e.submitter;
    btn.disabled = true;
    btn.textContent = 'Procesando...';

    await fetch(`/api/carritos/${carritoId}`, { method: 'DELETE' });
    localStorage.removeItem('carritoId');
    localStorage.removeItem('lineasMolienda');

    btn.textContent = '¡Pedido confirmado!';
    document.getElementById('seccion-carrito').innerHTML =
      '<p style="text-align:center; font-size:1.2rem; padding:40px;">¡Gracias por tu compra! Tu pedido ha sido registrado.</p>';
  });
});
