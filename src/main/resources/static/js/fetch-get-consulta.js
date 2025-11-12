/**
 * Busca (GET) as consultas na API e preenche a tabela HTML.
 * Usamos 'async' para poder usar 'await' e deixar o c�digo mais limpo.
 */
async function carregarConsultas() {
  
  // 1. Define o endpoint do seu GET
  const url = '/consultas'; // <<< VERIFIQUE SEU @GetMapping

  // Seleciona o corpo da tabela (onde as linhas ser�o inseridas)
  const tabelaBody = document.querySelector("#tabelaConsultas tbody");

  try {
    // 2. Faz a requisi��o GET
    const response = await fetch(url);

    // 3. Trata erros da API
    if (!response.ok) {
      alert('Erro ao carregar as consultas. A API pode estar fora do ar.');
      console.error('Erro na resposta da API:', response.statusText);
      return; // Para a execu��o
    }

    // 4. Converte a resposta em JSON (o array de consultas)
    const consultas = await response.json();

    // 5. [IMPORTANTE] Limpa a tabela antes de adicionar os novos dados
    // Isso previne que a lista seja duplicada a cada recarga.
    tabelaBody.innerHTML = '';

    // 6. Verifica se o array est� vazio
    if (consultas.length === 0) {
      tabelaBody.innerHTML = '<tr><td colspan="8" class="text-center">Nenhuma consulta encontrada.</td></tr>';
      return;
    }

    // 7. Itera (loop) sobre o array de consultas e cria as linhas da tabela
    consultas.forEach(consulta => {
      // Cria uma nova linha (tr)
      const linha = document.createElement('tr');

      // Preenche a linha com as c�lulas (td) usando os dados do JSON
      // A ordem deve bater exatamente com o seu <thead>
      linha.innerHTML = `
        <td>${consulta.id}</td>
        <td class="text-nowrap"">${consulta.nomeCliente}</td>
        <td class="text-nowrap">${consulta.documento}</td>
        <td class="text-nowrap">${consulta.contato}</td>
        <td class="text-nowrap">${consulta.dataHora}</td>
        <td>${consulta.status}</td>
        <td class="text-nowrap">${consulta.observacoes}</td>
        <td class="text-nowrap">${consulta.nomeMedico}</td>
        <td class="text-nowrap">
          <button class="btn btn-warning btn-sm btn-editar" data-id="${consulta.id}">Editar</button>
          <button class="btn btn-danger btn-sm btn-excluir" data-id="${consulta.id}">Excluir</button>
        </td>
      `;
      // Adiciona a linha preenchida ao corpo da tabela
      tabelaBody.appendChild(linha);
    });

  } catch (error) {
    // 8. Trata erros de rede (ex: sem internet)
    console.error('Erro de rede ao buscar consultas:', error);
    alert('N�o foi poss�vel conectar ao servidor para buscar as consultas.');
  }
}

// Seleciona o corpo da tabela (faça isso UMA vez, fora da função)
const tabelaBody = document.querySelector("#tabelaConsultas tbody");

// Adiciona um "escutador de cliques" GERAL no corpo da tabela
tabelaBody.addEventListener('click', function(event) {
  
  // 'event.target' é o elemento exato que foi clicado (ex: o <button>)
  const elementoClicado = event.target;

  // Verifica se o clique foi em um botão de EXCLUIR
  if (elementoClicado.classList.contains('btn-excluir')) {
    
    // Pega o ID que guardamos no 'data-id'
    const id = elementoClicado.dataset.id;
    
    // Pede confirmação ao usuário
    if (confirm(`Tem certeza que deseja excluir a consulta ID ${id}?`)) {
      excluirConsulta(id); // Chama a função para fazer o DELETE
    }
  }

  // Verifica se o clique foi em um botão de EDITAR
  if (elementoClicado.classList.contains('btn-editar')) {
    const id = elementoClicado.dataset.id;
    // Por enquanto, apenas avisamos. O "Editar" é mais complexo.
    // Chama a fun��o para buscar os dados e preencher o modal
    abrirModalParaEdicao(id);
    // (O próximo passo seria abrir um modal ou preencher o formulário)
  }
});

/**
 * Envia uma requisi��o DELETE para a API
 */
async function excluirConsulta(id) {
  
  // A URL para o endpoint de delete (ex: /api/consultas/3)
  const url = `/consultas/${id}`; // <<< VERIFIQUE SEU @DeleteMapping

  try {
    const response = await fetch(url, {
      method: 'DELETE'
    });

    if (response.ok) {
      alert('Consulta excluída com sucesso!');
      
      // [IMPORTANTE]
      // Ap�s excluir, mandamos carregar a lista de novo 
      // para que o item sumido desapare�a da tabela.
      carregarConsultas(); 
      
    } else {
      // Tenta ler a mensagem de erro da API
      const errorData = await response.json();
      alert(`Erro ao excluir: ${errorData.message || 'Erro desconhecido'}`);
    }

  } catch (error) {
    console.error('Erro de rede ao excluir:', error);
    alert('N�o foi poss�vel conectar ao servidor para excluir.');
  }
}

/**
 * Busca uma consulta por ID e preenche o modal de edição.
 */
async function abrirModalParaEdicao(id) {
  const url = `/consultas/${id}`; // <<< VERIFIQUE SEU ENDPOINT GET (por ID)

  try {
    const response = await fetch(url);
    if (!response.ok) {
      throw new Error('Não foi possível carregar os dados da consulta.');
    }
    const consulta = await response.json();

    // 1. Pega a instância do Modal do Bootstrap
    const modalElement = document.getElementById('modalEditarStatus');
    const modal = new bootstrap.Modal(modalElement);

    // 2. Preenche os campos do modal com os dados buscados
    document.getElementById('edit-id').value = consulta.id; // Guarda o ID no campo escondido
    document.getElementById('edit-status').value = consulta.status; // Define o <select>
    
    // Mostra para o usuário quem ele está editando
    const info = `ID ${consulta.id} - ${consulta.nomeCliente} (${consulta.dataHora})`;
    document.getElementById('edit-info').textContent = info;

    // 3. Mostra o modal
    modal.show();

  } catch (error) {
    console.error('Erro ao buscar consulta por ID:', error);
    alert('Erro ao carregar dados para edição.');
  }
}

// Seleciona o formul�rio do modal
const formEditar = document.getElementById('formEditarStatus');

// Adiciona o 'escutador' de envio
formEditar.addEventListener('submit', function(event) {
  event.preventDefault(); // Previne o recarregamento da p�gina

  // Pega os dados do formul�rio do modal
  const id = document.getElementById('edit-id').value;
  const novoStatus = document.getElementById('edit-status').value;

  // Monta o objeto (DTO) que o backend espera.
  // A maioria das APIs PATCH espera APENAS o campo que mudou.
  const dadosAtualizados = {
    novoStatus: novoStatus
  };

  // Chama a fun��o que faz o PATCH
  atualizarConsulta(id, dadosAtualizados);
});

/**
 * Envia a requisi��o PATCH para atualizar a consulta
 */
async function atualizarConsulta(id, dados) {
  const url = `/consultas/${id}/status`; // <<< VERIFIQUE SEU @PatchMapping

  try {
    const response = await fetch(url, {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(dados)
    });

    if (response.ok) {
      alert('Status atualizado com sucesso!');
      
      // Fecha o modal (usando a API do Bootstrap)
      const modalElement = document.getElementById('modalEditarStatus');
      const modal = bootstrap.Modal.getInstance(modalElement);
      modal.hide();

      // Recarrega a tabela para mostrar o status atualizado
      carregarConsultas();

    } else {
      // Tenta ler o erro de valida��o
      const errorData = await response.json();
      alert(`Erro ao atualizar: ${errorData.message || 'Erro desconhecido'}`);
    }

  } catch (error) {
    console.error('Erro de rede ao atualizar:', error);
    alert('N�o foi poss�vel conectar ao servidor para atualizar.');
  }
}