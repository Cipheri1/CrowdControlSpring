USE crowdcontrol_db;
DELETE c1 FROM chamado c1
JOIN chamado c2 ON c1.titulo = c2.titulo AND c1.solicitante = c2.solicitante
               AND c1.descricao = c2.descricao AND c1.id > c2.id
WHERE c1.id > 0;
