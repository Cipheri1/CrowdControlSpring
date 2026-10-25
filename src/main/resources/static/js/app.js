// Front-end ligado à API REST do servidor Spring: os dados vêm do MySQL.

// O que cada lista mostra: colunas, títulos e botões de filtro
const CONF = {
  usuarios: { cols: ['nome', 'email', 'cargo', 'status'], heads: ['Nome', 'E-mail', 'Cargo', 'Status'] },
  ordens: {
    cols: ['titulo', 'prioridade', 'status', 'responsavel', 'prazo'],
    heads: ['Título', 'Prioridade', 'Status', 'Responsável', 'Prazo'],
    filtros: ['Todos', 'Pendente', 'Em andamento', 'Concluída', 'Finalizada']
  },
  chamados: {
    cols: ['titulo', 'solicitante', 'categoria', 'prioridade', 'status'],
    heads: ['Título', 'Solicitante', 'Categoria', 'Prioridade', 'Status'],
    filtros: ['Todos', 'Aberto', 'Em andamento', 'Resolvido']
  }
};

const pagina = document.body.dataset.pagina;

const esc = s => String(s ?? '').replace(/[&<>"']/g,
  c => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[c]));

function formatar(coluna, valor) {
  if (coluna === 'prazo') return valor ? valor.split('-').reverse().join('/') : '—';
  if (coluna === 'status' || coluna === 'prioridade') return '<span class="badge">' + esc(valor) + '</span>';
  return esc(valor || '—');
}

// Chamada à API. Sem login (401) volta para a tela de login; erros do servidor viram mensagem.
async function api(caminho, opcoes = {}) {
  const resp = await fetch('/api' + caminho, { headers: { 'Content-Type': 'application/json' }, ...opcoes });
  if (resp.status === 401 && pagina !== 'login') {
    location.replace('login.html');
    throw new Error('Não autenticado');
  }
  const corpo = resp.status === 204 ? null : await resp.json().catch(() => null);
  if (!resp.ok) throw new Error((corpo && corpo.erro) || 'Erro ao falar com o servidor');
  return corpo;
}

function montarMenu(nome) {
  const itens = [['dashboard', 'Dashboard'], ['usuarios', 'Usuários'], ['ordens', 'Ordens de serviço'], ['chamados', 'Chamados']];
  document.getElementById('menu').innerHTML =
    '<div class="marca"><img src="img/logo-branco.svg" alt=""><span>Crowd<b>Control</b></span></div>' +
    itens.map(([p, t]) => '<a href="' + p + '.html"' + (p === pagina ? ' class="ativo"' : '') + '>' + t + '</a>').join('') +
    '<a href="#" id="sair">Sair</a>';
  document.getElementById('sair').onclick = e => {
    e.preventDefault();
    api('/logout', { method: 'POST' }).catch(() => {}).finally(() => { location.href = 'login.html'; });
  };
  document.getElementById('ola').textContent = 'Olá, ' + nome;
}

function montarLogin() {
  const form = document.getElementById('form');
  const msg = document.getElementById('msg');
  form.addEventListener('submit', async e => {
    e.preventDefault();
    msg.textContent = '';
    if (!validarFormulario(form)) return;
    try {
      await api('/login', {
        method: 'POST',
        body: JSON.stringify({ email: form.elements.email.value.trim(), senha: form.elements.senha.value })
      });
      location.href = 'dashboard.html';
    } catch (erro) {
      msg.textContent = erro.message; // "Email ou senha inválidos!" vem do servidor
    }
  });
}

async function montarDashboard() {
  const d = await api('/dashboard');
  const set = (id, v) => { document.getElementById(id).textContent = v; };
  set('n-ativos', d.usuariosAtivos);
  set('n-usuarios', d.totalUsuarios);
  set('n-ordens', d.totalOrdens);
  set('n-andamento', d.ordensEmAndamento);
  set('n-concluidas', d.ordensConcluidas);
  const alerta = document.getElementById('alerta');
  alerta.classList.toggle('ok', d.ordensPendentes === 0);
  alerta.textContent = d.mensagem + '. ' + d.orientacao;
}

function montarLista() {
  const cfg = CONF[pagina];
  const form = document.getElementById('form'), msg = document.getElementById('msg');
  const corpo = document.getElementById('corpo'), filtrosDiv = document.getElementById('filtros');
  let lista = [], filtro = 'Todos', editando = null;

  // usuários são identificados pelo e-mail; ordens e chamados, pelo id
  const chave = it => pagina === 'usuarios' ? it.email : it.id;
  const caminhoDe = it => '/' + pagina + '/' + encodeURIComponent(chave(it));
  const aviso = (texto, erro) => { msg.textContent = texto; msg.className = erro ? 'erro' : ''; };

  document.getElementById('cab').innerHTML = '<tr>' + cfg.heads.map(h => '<th>' + h + '</th>').join('') + '<th>Ações</th></tr>';

  function desenhar() {
    corpo.innerHTML = lista.map((it, i) =>
      '<tr>' + cfg.cols.map(c => '<td>' + formatar(c, it[c]) + '</td>').join('') +
      '<td><button data-editar="' + i + '">Editar</button> <button class="perigo" data-excluir="' + i + '">Excluir</button></td></tr>'
    ).join('') || '<tr><td colspan="' + (cfg.cols.length + 1) + '">Nenhum registro neste filtro.</td></tr>';
  }

  async function carregar() {
    try {
      const filtrar = cfg.filtros && filtro !== 'Todos';
      lista = await api('/' + pagina + (filtrar ? '?status=' + encodeURIComponent(filtro) : ''));
      desenhar();
    } catch (e) { aviso(e.message, true); }
  }

  function limpar() {
    form.reset();
    editando = null;
    delete form.dataset.editando;
    if (form.elements.email) form.elements.email.readOnly = false;
    form.querySelectorAll('.erro').forEach(x => { if (x !== msg) x.textContent = ''; });
    form.querySelectorAll('.invalido').forEach(x => x.classList.remove('invalido'));
  }

  if (cfg.filtros) {
    filtrosDiv.innerHTML = cfg.filtros.map(f => '<button data-f="' + f + '"' + (f === 'Todos' ? ' class="ativo"' : '') + '>' + f + '</button>').join('');
    filtrosDiv.onclick = e => {
      if (!e.target.dataset.f) return;
      filtro = e.target.dataset.f;
      filtrosDiv.querySelectorAll('button').forEach(b => b.classList.toggle('ativo', b.dataset.f === filtro));
      carregar(); // o filtro é feito pelo servidor (?status=...)
    };
  }

  corpo.onclick = async e => {
    const b = e.target;
    if (b.dataset.excluir !== undefined && confirm('Excluir este registro?')) {
      try {
        await api(caminhoDe(lista[Number(b.dataset.excluir)]), { method: 'DELETE' });
        limpar();
        aviso('Registro excluído.');
        await carregar();
      } catch (erro) { aviso(erro.message, true); }
    }
    if (b.dataset.editar !== undefined) {
      editando = lista[Number(b.dataset.editar)];
      form.dataset.editando = '1';
      Array.from(form.elements).forEach(el => { if (el.name) el.value = editando[el.name] ?? ''; });
      if (form.elements.email) form.elements.email.readOnly = true; // o e-mail identifica o usuário
      aviso('Editando: altere os campos e salve.');
      form.scrollIntoView({ behavior: 'smooth' });
    }
  };

  document.getElementById('cancelar').onclick = () => { limpar(); aviso(''); };

  form.addEventListener('submit', async e => {
    e.preventDefault();
    if (!validarFormulario(form)) return;
    const obj = Object.fromEntries(Array.from(new FormData(form)).map(([k, v]) => [k, v.trim()]));
    const mensagem = editando ? 'Alterações salvas.' : 'Registro adicionado.';
    try {
      await api('/' + pagina + (editando ? '/' + encodeURIComponent(chave(editando)) : ''),
        { method: editando ? 'PUT' : 'POST', body: JSON.stringify(obj) });
      limpar();
      await carregar();
      aviso(mensagem);
    } catch (erro) { aviso(erro.message, true); } // ex.: "Email já cadastrado!"
  });

  carregar();
}

async function iniciar() {
  if (pagina === 'login') { montarLogin(); return; }
  const sessao = await api('/sessao'); // sem login, o api() redireciona para login.html
  montarMenu(sessao.nome);
  if (pagina === 'dashboard') montarDashboard(); else montarLista();
}
iniciar().catch(() => {});
