// Modal de email muy chulo

let _modalEmailResolve = null;

function inyectarModalEmail() {
  const modal = document.createElement('div');
  modal.id = 'modal-email';
  modal.style.cssText = 'display:none;position:fixed;inset:0;background:rgba(0,0,0,.5);z-index:9999;align-items:center;justify-content:center;';
  modal.innerHTML = `
    <div style="background:#fff;border-radius:12px;padding:36px;width:90%;max-width:400px;box-shadow:0 8px 32px rgba(0,0,0,.2);">
      <h2 id="modal-email-titulo" style="margin-bottom:8px;color:#333;"></h2>
      <p id="modal-email-desc" style="color:#666;margin-bottom:24px;font-size:.9rem;"></p>
      <form id="form-modal-email" onsubmit="submitModalEmail(event)">
        <div style="margin-bottom:16px;">
          <label for="modal-email-input" style="display:block;margin-bottom:6px;font-weight:600;font-size:.9rem;">Email</label>
          <input type="email" id="modal-email-input" placeholder="tu@email.com" required
            style="width:100%;padding:12px;border:1px solid #ddd;border-radius:8px;font-size:1rem;font-family:inherit;">
        </div>
        <p id="modal-email-error" style="color:#c0392b;font-size:.85rem;margin-bottom:12px;min-height:1.1em;"></p>
        <button type="submit" class="btn" style="width:100%;padding:14px;font-size:1rem;">Continuar</button>
      </form>
      <button onclick="cancelarModalEmail()" style="margin-top:12px;width:100%;padding:10px;background:none;border:1px solid #ddd;border-radius:8px;cursor:pointer;font-family:inherit;font-size:.9rem;">Cancelar</button>
    </div>`;
  modal.addEventListener('click', (e) => { if (e.target === modal) cancelarModalEmail(); });
  document.body.appendChild(modal);
}

function abrirModalEmail(titulo, descripcion) {
  return new Promise((resolve) => {
    _modalEmailResolve = resolve;
    document.getElementById('modal-email-titulo').textContent = titulo;
    document.getElementById('modal-email-desc').textContent = descripcion;
    document.getElementById('modal-email-input').value = '';
    document.getElementById('modal-email-error').textContent = '';
    const modal = document.getElementById('modal-email');
    modal.style.display = 'flex';
    document.getElementById('modal-email-input').focus();
  });
}

function cerrarModalEmail() {
  document.getElementById('modal-email').style.display = 'none';
  _modalEmailResolve = null;
}

function cancelarModalEmail() {
  if (_modalEmailResolve) _modalEmailResolve(null);
  cerrarModalEmail();
}

function submitModalEmail(e) {
  e.preventDefault();
  const email = document.getElementById('modal-email-input').value.trim();
  if (_modalEmailResolve) _modalEmailResolve(email);
  cerrarModalEmail();
}

// Sesión

function getEmailSesion() {
  return localStorage.getItem('carritoEmail');
}

function guardarSesion(idCarrito, emailUsuario) {
  localStorage.setItem('carritoId', idCarrito);
  localStorage.setItem('carritoEmail', emailUsuario);
}

function cerrarSesion() {
  localStorage.removeItem('carritoId');
  localStorage.removeItem('carritoEmail');
  localStorage.removeItem('lineasMolienda');
}

function actualizarBotonLogin() {
  const btn = document.getElementById('btn-login');
  if (!btn) return;
  const email = getEmailSesion();
  if (email) {
    btn.textContent = `Iniciado como ${email}`;
    btn.style.background = 'transparent';
    btn.style.color = 'var(--main-color)';
    btn.style.border = '1px solid var(--main-color)';
    btn.style.cursor = 'default';
    btn.onclick = null;
  } else {
    btn.textContent = 'Login';
    btn.style.background = '';
    btn.style.color = '';
    btn.style.border = '';
    btn.style.cursor = '';
    btn.onclick = hacerLogin;
  }
}

async function hacerLogin() {
  const email = await abrirModalEmail('Cargar mi carrito', 'Introduce tu email para recuperar tu carrito guardado.');
  if (!email) return;

  const res = await fetch(`/api/carritos/buscar?email=${encodeURIComponent(email)}`);
  if (!res.ok) {
    alert('No se encontró carrito para ese email.');
    return;
  }

  const carrito = await res.json();
  guardarSesion(carrito.idCarrito, carrito.emailUsuario);
  actualizarBotonLogin();
  if (typeof loadCarrito === 'function') loadCarrito();
}

// Carrito

async function getOrCreateCarrito() {
  const id = localStorage.getItem('carritoId');
  if (id) return parseInt(id);

  const email = await abrirModalEmail('¿Quién eres?', 'Introduce tu email para añadir productos al carrito.');
  if (!email || !email.includes('@')) return null;

  // Buscar carrito existente
  const buscar = await fetch(`/api/carritos/buscar?email=${encodeURIComponent(email)}`);
  if (buscar.ok) {
    const carrito = await buscar.json();
    guardarSesion(carrito.idCarrito, carrito.emailUsuario);
    actualizarBotonLogin();
    return carrito.idCarrito;
  }

  // Crear carrito nuevo
  const crear = await fetch('/api/carritos', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ idUsuario: Date.now(), emailUsuario: email })
  });

  if (!crear.ok) {
    alert('Error creando el carrito. Inténtalo de nuevo.');
    return null;
  }

  const carrito = await crear.json();
  guardarSesion(carrito.idCarrito, carrito.emailUsuario);
  actualizarBotonLogin();
  return carrito.idCarrito;
}

function getMoliendaMap() {
  return JSON.parse(localStorage.getItem('lineasMolienda') || '{}');
}

function saveMolienda(idLinea, molienda) {
  const map = getMoliendaMap();
  map[idLinea] = molienda;
  localStorage.setItem('lineasMolienda', JSON.stringify(map));
}

function removeMolienda(idLinea) {
  const map = getMoliendaMap();
  delete map[idLinea];
  localStorage.setItem('lineasMolienda', JSON.stringify(map));
}

// Catálogo

const PRODUCTOS = [
  { id: 1, nombre: "Etiopía Yirgacheffe", precio: 18.50, notas: "Notas florales, cítricos y té negro.",      imagen: "product-1.png", origen: "Etiopía, Región de Yirgacheffe", altitud: "1,900 - 2,100 msnm", proceso: "Lavado",  tueste: "Medio-Ligero" },
  { id: 2, nombre: "Colombia Huila",       precio: 16.00, notas: "Chocolate, caramelo y frutos rojos.",       imagen: "product-2.png", origen: "Colombia, Huila",                 altitud: "1,600 - 1,900 msnm", proceso: "Lavado",  tueste: "Medio"       },
  { id: 3, nombre: "Brasil Cerrado",       precio: 14.50, notas: "Frutos secos, cuerpo denso y baja acidez.", imagen: "product-3.png", origen: "Brasil, Cerrado",                  altitud: "800 - 1,100 msnm",   proceso: "Natural", tueste: "Medio-Oscuro" },
  { id: 4, nombre: "Guatemala Antigua",    precio: 17.00, notas: "Especiado, ahumado y chocolate amargo.",    imagen: "product-4.png", origen: "Guatemala, Antigua",               altitud: "1,500 - 1,700 msnm", proceso: "Lavado",  tueste: "Oscuro"      },
  { id: 5, nombre: "Kenia AA",             precio: 20.00, notas: "Acidez brillante, grosella negra y vino.", imagen: "product-5.png", origen: "Kenia, Nyeri",                     altitud: "1,700 - 2,000 msnm", proceso: "Lavado",  tueste: "Medio"       },
  { id: 6, nombre: "Sumatra Mandheling",   precio: 15.50, notas: "Terroso, especiado y cuerpo muy pesado.",  imagen: "product-6.png", origen: "Indonesia, Sumatra",               altitud: "1,000 - 1,500 msnm", proceso: "Húmedo",  tueste: "Oscuro"      },
];

document.addEventListener('DOMContentLoaded', () => {
  inyectarModalEmail();
  actualizarBotonLogin();

  const btnLogin = document.getElementById('btn-login');
  if (btnLogin && !getEmailSesion()) {
    btnLogin.addEventListener('click', hacerLogin);
  }
});
