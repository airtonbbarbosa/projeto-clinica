// @ts-nocheck

document.addEventListener('DOMContentLoaded', function() {
    const roleAtendente = document.getElementById('roleAtendente');
    const roleMedico = document.getElementById('roleMedico');
    const userRole = document.getElementById('userRole');
    const cadastroForm = document.getElementById('cadastroForm');

    if (!roleAtendente || !roleMedico || !userRole || !cadastroForm) return;

    // Seleção de tipo de usuário
    roleAtendente.addEventListener('click', () => {
        userRole.value = 'Atendente';
        roleAtendente.classList.add('active-role');
        roleMedico.classList.remove('active-role');
    });

    roleMedico.addEventListener('click', () => {
        userRole.value = 'Medico';
        roleMedico.classList.add('active-role');
        roleAtendente.classList.remove('active-role');
    });

    // Validação do formulário
    cadastroForm.addEventListener('submit', function(e) {
        e.preventDefault();

        const senha = document.getElementById('senhaCadastro').value;
        const confirma = document.getElementById('confirmaSenha').value;

        if (!userRole.value) {
            alert('Selecione se você é Atendente ou Médico.');
            return;
        }

        if (senha !== confirma) {
            alert('As senhas não coincidem!');
            return;
        }

        alert(`Cadastro realizado com sucesso! Tipo: ${userRole.value}`);

        // Reset do formulário e modal
        cadastroForm.reset();
        userRole.value = '';
        roleAtendente.classList.remove('active-role');
        roleMedico.classList.remove('active-role');

        const modalEl = document.getElementById('suporteModal');
        const modal = bootstrap.Modal.getOrCreateInstance(modalEl);
        modal.hide();
    });
});
