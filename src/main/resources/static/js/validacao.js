// Validações dos formulários (login e cadastros). Usado por app.js.
const EMAIL = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

function erroCampo(c) {
  const v = c.value.trim();
  // Ao editar um usuário, senha em branco significa "manter a atual"
  if (c.required && !v && !(c.name === 'senha' && c.form.dataset.editando)) return 'Campo obrigatório';
  if (v && c.type === 'email' && !EMAIL.test(v)) return 'Informe um e-mail válido';
  if (v && c.minLength > 0 && v.length < c.minLength) return 'Mínimo de ' + c.minLength + ' caracteres';
  // Prazo de uma ordem nova não pode estar no passado (ao editar, não se aplica)
  if (v && c.dataset.futuro && !c.form.dataset.editando && v < new Date().toISOString().slice(0, 10)) {
    return 'O prazo não pode ser uma data passada';
  }
  return '';
}

function mostrarErro(c, msg) {
  let el = c.parentElement.querySelector('.erro');
  if (!el) {
    el = document.createElement('small');
    el.className = 'erro';
    c.parentElement.appendChild(el);
  }
  el.textContent = msg;
  c.classList.toggle('invalido', !!msg);
}

function validarFormulario(form) {
  let ok = true;
  form.querySelectorAll('input, select, textarea').forEach(c => {
    const msg = erroCampo(c);
    mostrarErro(c, msg);
    if (msg) ok = false;
  });
  return ok;
}

// Depois de um erro, revalida o campo enquanto a pessoa digita
document.addEventListener('input', e => {
  if (e.target.classList.contains('invalido')) mostrarErro(e.target, erroCampo(e.target));
});
