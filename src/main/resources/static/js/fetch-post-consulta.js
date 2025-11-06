document.addEventListener("DOMContentLoaded", function() {
  
  const form = document.getElementById('formAgendar');

  form.addEventListener('submit', function(event) {
    event.preventDefault();

    // 1. Coleta os dados dos inputs
    const nome = document.getElementById('form-nome').value;
    const documento = document.getElementById('form-documento').value;
    const telefone = document.getElementById('form-contato').value;
    const dataHora = document.getElementById('form-data').value; // Pega a string de texto
    const observacoes = document.getElementById('form-obs').value;
    const medicoId = document.getElementById('form-medico').value;

    // 2. Monta o objeto JSON (DTO)
    // As chaves aqui devem ser ID�NTICAS �s do seu ConsultaRequestDTO
    const dadosConsulta = {
      nomeCliente: nome,
      documento: documento,
      contato: telefone,
      dataHora: dataHora,       // Enviando a string de texto como o backend espera
      observacoes: observacoes,
      medicoId: medicoId        // Enviando o ID (Long)
    };

    // 3. Define a URL do seu endpoint
    const url = '/consultas'; // <<< VERIFIQUE SEU @PostMapping

    // (O resto do seu c�digo, 'submit', 'preventDefault', 'dadosConsulta', etc. fica igual)

// 4. Envia os dados com o FETCH
fetch(url, {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify(dadosConsulta) 
})
.then(response => {
  if (response.ok) {
    // SUCESSO
    alert('Consulta agendada com sucesso!');
    form.reset(); 
    return; // Encerra a execu��o
  } 
  
  // ----- AQUI EST� A MUDAN�A -----
  // Se a resposta N�O foi 'ok' (foi um erro 400, 500, etc.)
  // N�s pedimos para ler o corpo da resposta como JSON
  return response.json().then(errorData => {
    // 'errorData' � o objeto JSON que o Spring enviou
    console.error('Erro de valida��o do Backend:', errorData);

    let mensagemErro = 'Erro ao agendar:\n';

    // O Spring (com @Valid) geralmente retorna um array 'errors'
    if (errorData.errors && Array.isArray(errorData.errors)) {
      errorData.errors.forEach(err => {
        // Ex: "O campo nomeCliente n�o pode estar em branco"
        mensagemErro += `- ${err.defaultMessage}\n`; 
      });
    } else if (errorData.message) {
      mensagemErro += errorData.message;
    } else {
      mensagemErro += 'O servidor retornou um erro, mas sem detalhes.';
    }

    alert(mensagemErro);
  });
})
.catch(error => {
  // Erro de rede (API fora do ar, CORS, etc.)
  console.error('Erro de rede:', error);
  alert('N�o foi poss�vel conectar ao servidor.');
});

  });
});