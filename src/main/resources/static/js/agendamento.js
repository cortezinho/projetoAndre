const API_CONSULTAS = "http://localhost:8089/consultas";
const API_PACIENTES = "http://localhost:8089/pacientes";
const API_MEDICOS = "http://localhost:8089/medicos";

document.addEventListener("DOMContentLoaded", () => {
  carregarPacientesSelect();
  carregarMedicosSelect();
  listarConsultas();
});

// --- CARREGAR SELECT DE PACIENTES ---
async function carregarPacientesSelect() {
  try {
    const response = await fetch(API_PACIENTES);
    const data = await response.json();
    const select = document.getElementById("selectPaciente");

    select.innerHTML = '<option value="">Selecione um paciente...</option>';

    if (data.content) {
      data.content.forEach((p) => {
        const option = document.createElement("option");
        option.value = p.id;
        option.text = `${p.nome} (CPF: ${p.cpf})`;
        select.appendChild(option);
      });
    }
  } catch (error) {
    console.error("Erro ao carregar pacientes:", error);
    document.getElementById("selectPaciente").innerHTML =
      "<option>Erro ao carregar</option>";
  }
}

// --- CARREGAR SELECT DE MÉDICOS ---
async function carregarMedicosSelect() {
  try {
    const response = await fetch(API_MEDICOS);
    const data = await response.json();
    const select = document.getElementById("selectMedico");

    select.innerHTML =
      '<option value="">Selecione um médico (ou aleatório)...</option>';

    if (data.content) {
      data.content.forEach((m) => {
        const option = document.createElement("option");
        option.value = m.id;
        option.text = `${m.nome} - ${m.especialidade}`;
        select.appendChild(option);
      });
    }
  } catch (error) {
    console.error("Erro ao carregar médicos:", error);
  }
}

// --- LISTAR CONSULTAS (TABELA DIREITA) ---
async function listarConsultas() {
  try {
    const response = await fetch(API_CONSULTAS);
    const data = await response.json();
    const tbody = document.querySelector("#tabelaConsultas tbody");
    tbody.innerHTML = "";

    if (data.content && data.content.length > 0) {
      data.content.forEach((c) => {
        const dataFormatada = new Date(c.data).toLocaleString("pt-BR");
        const tr = document.createElement("tr");
        tr.innerHTML = `<td>${c.id}</td><td>${c.medico}</td><td>${c.paciente}</td><td>${dataFormatada}</td>`;
        tbody.appendChild(tr);
      });
    } else {
      tbody.innerHTML =
        '<tr><td colspan="4" style="text-align:center;">Nenhuma consulta agendada.</td></tr>';
    }
  } catch (error) {
    console.error("Erro consultas:", error);
  }
}

async function listarConsultas() {
  try {
    const response = await fetch("http://localhost:8089/consultas"); // Verifique se a URL está correta
    const data = await response.json();
    const tbody = document.querySelector("#tabelaConsultas tbody");
    tbody.innerHTML = "";

    if (data.content && data.content.length > 0) {
      data.content.forEach((c) => {
        const dataFormatada = new Date(c.data).toLocaleString("pt-BR");

        // Verifica se tem motivo de cancelamento
        let statusTexto = "Agendada";
        let classeCor = "status-ativo"; // Para usar no CSS se quiser

        if (c.motivoCancelamento) {
          statusTexto = `Cancelada: ${c.motivoCancelamento}`;
          classeCor = "status-cancelado";
        }

        const tr = document.createElement("tr");
        // Adiciona uma classe na linha para estilizar (veja o CSS abaixo)
        tr.classList.add(classeCor);

        tr.innerHTML = `
                    <td>${c.id}</td>
                    <td>${c.medico}</td>
                    <td>${c.paciente}</td>
                    <td>${dataFormatada}</td>
                    <td>${statusTexto}</td>
                `;
        tbody.appendChild(tr);
      });
    } else {
      tbody.innerHTML =
        '<tr><td colspan="5" style="text-align:center;">Nenhuma consulta encontrada.</td></tr>';
    }
  } catch (error) {
    console.error("Erro consultas:", error);
  }
}

// --- AGENDAR CONSULTA (POST) ---
document
  .getElementById("formAgendamento")
  .addEventListener("submit", async (e) => {
    e.preventDefault();
    const resultDiv = document.getElementById("resultAgendamento");
    resultDiv.style.display = "none";

    // Pega valor dos Selects
    const idPaciente = document.getElementById("selectPaciente").value;
    const idMedico = document.getElementById("selectMedico").value;
    const especialidade = document.getElementById("especialidade").value;
    const dataHora = document.getElementById("data").value;

    if (!idPaciente) {
      alert("Selecione um paciente!");
      return;
    }

    const payload = { idPaciente: idPaciente, data: dataHora };
    if (idMedico) payload.idMedico = idMedico;
    if (especialidade) payload.especialidade = especialidade;

    try {
      const response = await fetch(API_CONSULTAS, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });

      const data = await response.json().catch(() => ({}));

      if (response.ok) {
        resultDiv.className = "result success";
        resultDiv.innerHTML = `Sucesso! Consulta agendada.`;
        resultDiv.style.display = "block";
        listarConsultas();
      } else {
        throw new Error(data.message || "Erro ao agendar.");
      }
    } catch (error) {
      resultDiv.className = "result error";
      resultDiv.innerText = error.message;
      resultDiv.style.display = "block";
    }
  });

// --- CANCELAR CONSULTA (DELETE) ---
document
  .getElementById("formCancelamento")
  .addEventListener("submit", async (e) => {
    e.preventDefault();
    const resultDiv = document.getElementById("resultCancelamento");

    const payload = {
      idConsulta: document.getElementById("idConsulta").value,
      motivo: document.getElementById("motivo").value,
    };

    try {
      const response = await fetch(API_CONSULTAS, {
        method: "DELETE",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });

      if (response.status === 204) {
        resultDiv.className = "result success";
        resultDiv.innerText = "Consulta cancelada!";
        resultDiv.style.display = "block";
        listarConsultas();
        document.getElementById("formCancelamento").reset();
      } else {
        throw new Error("Erro ao cancelar.");
      }
    } catch (error) {
      resultDiv.className = "result error";
      resultDiv.innerText = error.message;
      resultDiv.style.display = "block";
    }
  });
