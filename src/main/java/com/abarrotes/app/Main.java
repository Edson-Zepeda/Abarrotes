package com.abarrotes.app;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            printMenu();
            if (!scanner.hasNextLine()) {
                System.out.println("Sistema finalizado.");
                break;
            }
            String option = scanner.nextLine().trim();

            switch (option) {
                case "1" -> System.out.println("Modulo de inventario: pendiente de implementar.");
                case "2" -> System.out.println("Modulo de clientes: pendiente de implementar.");
                case "3" -> System.out.println("Modulo de proveedores: pendiente de implementar.");
                case "4" -> System.out.println("Modulo de compras: pendiente de implementar.");
                case "5" -> System.out.println("Modulo de ventas: pendiente de implementar.");
                case "6" -> System.out.println("Modulo de corte de caja: pendiente de implementar.");
                case "7" -> System.out.println("Modulo de usuarios: pendiente de implementar.");
                case "0" -> {
                    running = false;
                    System.out.println("Sistema finalizado.");
                }
                default -> System.out.println("Opcion invalida. Intenta nuevamente.");
            }
        }
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("==================================");
        System.out.println(" Sistema de Abarrotes - Equipo 6");
        System.out.println("==================================");
        System.out.println("1. Inventario");
        System.out.println("2. Clientes");
        System.out.println("3. Proveedores");
        System.out.println("4. Compras");
        System.out.println("5. Ventas");
        System.out.println("6. Corte de caja");
        System.out.println("7. Usuarios");
        System.out.println("0. Salir");
        System.out.print("Selecciona una opcion: ");
    }
}
