USE crediya_db;

INSERT INTO empleados (nombre, documento, rol, correo, salario) VALUES
('Laura Martinez', '1098765432', 'Asesora', 'laura@crediya.com', 2500000.00),
('Jorge Ramirez',  '1087654321', 'Cobrador', 'jorge@crediya.com', 1800000.00);

INSERT INTO clientes (nombre, documento, correo, telefono) VALUES
('Carlos Perez',  '1012345678', 'carlos@correo.com', '3001112233'),
('Maria Gomez',   '1023456789', 'maria@correo.com',  '3104445566'),
('Andres Rojas',  '1034567890', 'andres@correo.com', '3207778899');

INSERT INTO prestamos (cliente_id, empleado_id, monto, interes, cuotas, fecha_inicio, estado) VALUES
(1, 1, 2000000.00, 10.00, 6,  '2026-01-15', 'PENDIENTE'),
(2, 2, 1500000.00,  8.00, 12, '2026-06-01', 'PENDIENTE'),
(3, 1,  800000.00,  5.00, 4,  '2026-05-10', 'PAGADO'),
(2, 2,  500000.00, 12.00, 3,  '2026-03-01', 'PENDIENTE');

INSERT INTO pagos (prestamo_id, fecha_pago, monto) VALUES
(1, '2026-02-15', 370000.00),
(1, '2026-03-15', 370000.00),
(2, '2026-07-01', 135000.00),
(3, '2026-06-10', 210000.00),
(3, '2026-07-10', 210000.00),
(3, '2026-08-10', 210000.00),
(3, '2026-09-10', 210000.00);
