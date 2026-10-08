package com.crediya.app;

import com.crediya.excepciones.CrediYaException;
import com.crediya.modelo.*;
import com.crediya.persistencia.ArchivoTexto;
import com.crediya.servicio.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Menu {

    private final Scanner scanner = new Scanner(System.in);
    private final EmpleadoServicio empleados;
    private final ClienteServicio clientes;
    private final PrestamoServicio prestamos;
    private final PagoServicio pagos;
    private final ReporteServicio reportes;

    public Menu(EmpleadoServicio empleados, ClienteServicio clientes, PrestamoServicio prestamos,
                PagoServicio pagos, ReporteServicio reportes) {
        this.empleados = empleados;
        this.clientes = clientes;
        this.prestamos = prestamos;
        this.pagos = pagos;
        this.reportes = reportes;
    }

    public void iniciar() {
        int opcion;
        do {
            System.out.println();
            System.out.println("===== CREDIYA S.A.S. =====");
            System.out.println("1. Empleados");
            System.out.println("2. Clientes");
            System.out.println("3. Prestamos");
            System.out.println("4. Pagos");
            System.out.println("5. Reportes");
            System.out.println("6. Ver archivos de texto");
            System.out.println("0. Salir");
            opcion = leerEntero("Opcion: ");
            switch (opcion) {
                case 1: menuEmpleados(); break;
                case 2: menuClientes(); break;
                case 3: menuPrestamos(); break;
                case 4: menuPagos(); break;
                case 5: menuReportes(); break;
                case 6: menuArchivos(); break;
                case 0: System.out.println("Hasta pronto!"); break;
                default: System.out.println("Opcion no valida.");
            }
        } while (opcion != 0);
    }

    private interface Accion {
        void run() throws SQLException, CrediYaException;
    }

    private void ejecutar(Accion accion) {
        try {
            accion.run();
        } catch (CrediYaException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        }
    }

    private void menuEmpleados() {
        int opcion;
        do {
            System.out.println();
            System.out.println("-- EMPLEADOS --");
            System.out.println("1. Registrar");
            System.out.println("2. Listar");
            System.out.println("3. Buscar por id");
            System.out.println("0. Volver");
            opcion = leerEntero("Opcion: ");
            final int elegida = opcion;
            ejecutar(() -> {
                switch (elegida) {
                    case 1:
                        Empleado e = empleados.registrar(
                                leerTexto("Nombre: "), leerTexto("Documento: "), leerTexto("Rol: "),
                                leerTexto("Correo: "), leerDecimal("Salario: "));
                        System.out.println("Empleado registrado con id " + e.getId());
                        break;
                    case 2:
                        mostrarLista(empleados.listar());
                        break;
                    case 3:
                        System.out.println(empleados.buscarPorId(leerEntero("Id: ")));
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opcion no valida.");
                }
            });
        } while (opcion != 0);
    }

    private void menuClientes() {
        int opcion;
        do {
            System.out.println();
            System.out.println("-- CLIENTES --");
            System.out.println("1. Registrar");
            System.out.println("2. Listar");
            System.out.println("3. Consultar prestamos de un cliente");
            System.out.println("0. Volver");
            opcion = leerEntero("Opcion: ");
            final int elegida = opcion;
            ejecutar(() -> {
                switch (elegida) {
                    case 1:
                        Cliente c = clientes.registrar(
                                leerTexto("Nombre: "), leerTexto("Documento: "),
                                leerTexto("Correo: "), leerTexto("Telefono: "));
                        System.out.println("Cliente registrado con id " + c.getId());
                        break;
                    case 2:
                        mostrarLista(clientes.listar());
                        break;
                    case 3:
                        mostrarLista(prestamos.listarPorCliente(leerEntero("Id del cliente: ")));
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opcion no valida.");
                }
            });
        } while (opcion != 0);
    }

    private void menuPrestamos() {
        int opcion;
        do {
            System.out.println();
            System.out.println("-- PRESTAMOS --");
            System.out.println("1. Crear");
            System.out.println("2. Listar");
            System.out.println("3. Cambiar estado");
            System.out.println("0. Volver");
            opcion = leerEntero("Opcion: ");
            final int elegida = opcion;
            ejecutar(() -> {
                switch (elegida) {
                    case 1:
                        Prestamo p = prestamos.crear(leerEntero("Id del cliente: "), leerEntero("Id del empleado: "),
                                leerDecimal("Monto: "), leerDecimal("Interes (% sobre el monto): "),
                                leerEntero("Numero de cuotas (meses): "));
                        System.out.println("Prestamo creado:");
                        System.out.println(p);
                        break;
                    case 2:
                        mostrarLista(prestamos.listar());
                        break;
                    case 3:
                        int id = leerEntero("Id del prestamo: ");
                        String texto = leerTexto("Nuevo estado (PENDIENTE / PAGADO): ").toUpperCase();
                        try {
                            prestamos.cambiarEstado(id, EstadoPrestamo.valueOf(texto));
                            System.out.println("Estado actualizado.");
                        } catch (IllegalArgumentException ex) {
                            System.out.println("Estado no valido. Escribe PENDIENTE o PAGADO.");
                        }
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opcion no valida.");
                }
            });
        } while (opcion != 0);
    }

    private void menuPagos() {
        int opcion;
        do {
            System.out.println();
            System.out.println("-- PAGOS --");
            System.out.println("1. Registrar abono");
            System.out.println("2. Historico de pagos de un prestamo");
            System.out.println("0. Volver");
            opcion = leerEntero("Opcion: ");
            final int elegida = opcion;
            ejecutar(() -> {
                switch (elegida) {
                    case 1:
                        int prestamoId = leerEntero("Id del prestamo: ");
                        Pago pago = pagos.registrar(prestamoId, leerDecimal("Monto del abono: "));
                        System.out.println("Abono registrado: " + pago);
                        System.out.println(prestamos.buscarPorId(prestamoId));
                        break;
                    case 2:
                        mostrarLista(pagos.historial(leerEntero("Id del prestamo: ")));
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opcion no valida.");
                }
            });
        } while (opcion != 0);
    }

    private void menuReportes() {
        int opcion;
        do {
            System.out.println();
            System.out.println("-- REPORTES --");
            System.out.println("1. Prestamos activos");
            System.out.println("2. Prestamos vencidos");
            System.out.println("3. Clientes morosos");
            System.out.println("4. Cartera pendiente y total recaudado");
            System.out.println("5. Prestamos por empleado");
            System.out.println("6. Prestamo de mayor monto");
            System.out.println("7. Prestamos activos ordenados por saldo");
            System.out.println("0. Volver");
            opcion = leerEntero("Opcion: ");
            switch (opcion) {
                case 1: mostrarLista(reportes.prestamosActivos()); break;
                case 2: mostrarLista(reportes.prestamosVencidos()); break;
                case 3: mostrarLista(reportes.clientesMorosos()); break;
                case 4:
                    System.out.println("Cartera pendiente: $" + String.format("%,.2f", reportes.carteraPendiente()));
                    System.out.println("Total recaudado:   $" + String.format("%,.2f", reportes.totalRecaudado()));
                    break;
                case 5:
                    reportes.prestamosPorEmpleado().forEach(
                            (idEmpleado, cantidad) -> System.out.println("Empleado " + idEmpleado + ": " + cantidad + " prestamo(s)"));
                    break;
                case 6:
                    System.out.println(reportes.prestamoMayorMonto().map(Prestamo::toString).orElse("No hay prestamos."));
                    break;
                case 7: mostrarLista(reportes.prestamosOrdenadosPorSaldo()); break;
                case 0: break;
                default: System.out.println("Opcion no valida.");
            }
        } while (opcion != 0);
    }

    private void menuArchivos() {
        String[] nombres = {"empleados.txt", "clientes.txt", "prestamos.txt", "pagos.txt"};
        int opcion;
        do {
            System.out.println();
            System.out.println("-- ARCHIVOS --");
            System.out.println("1. empleados.txt");
            System.out.println("2. clientes.txt");
            System.out.println("3. prestamos.txt");
            System.out.println("4. pagos.txt");
            System.out.println("0. Volver");
            opcion = leerEntero("Opcion: ");
            final int elegida = opcion;
            ejecutar(() -> {
                if (elegida >= 1 && elegida <= 4) {
                    List<String> lineas = ArchivoTexto.leer(nombres[elegida - 1]);
                    if (lineas.isEmpty()) {
                        System.out.println("(archivo vacio)");
                    }
                    lineas.forEach(System.out::println);
                } else if (elegida != 0) {
                    System.out.println("Opcion no valida.");
                }
            });
        } while (opcion != 0);
    }

    private void mostrarLista(List<?> lista) {
        if (lista.isEmpty()) {
            System.out.println("(sin resultados)");
        } else {
            lista.forEach(System.out::println);
        }
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine();
    }

    private int leerEntero(String mensaje) {
        while (true) {
            try {
                return Integer.parseInt(leerTexto(mensaje).trim());
            } catch (NumberFormatException e) {
                System.out.println("Escribe un numero entero valido.");
            }
        }
    }

    private double leerDecimal(String mensaje) {
        while (true) {
            try {
                return Double.parseDouble(leerTexto(mensaje).trim().replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println("Escribe un numero valido (ej: 1500000 o 2.5).");
            }
        }
    }
}
