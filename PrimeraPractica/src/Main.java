import java.time.LocalDateTime;
import java.util.Scanner;

class Ticket {
    static int cantidad = 0;
    int id = ++cantidad;
    String nombre, desc;
    LocalDateTime creacion = LocalDateTime.now(), resolucion = null;
    Ticket(String nombre, String desc) { this.nombre = nombre; this.desc = desc; }
    public String toString() {
        return "ID: " + id + " | Creador: " + nombre + " | Desc: " + desc +
                " | Creado: " + creacion + " | Resuelto: " + (resolucion != null ? resolucion : "Pendiente");
    }
}

class Nodo {
    Ticket ticket;
    Nodo siguiente;
    Nodo(Ticket t) { this.ticket = t; }
}

class ListaResueltos {
    Nodo cabeza;
    void insertar(Ticket t) {
        Nodo nuevo = new Nodo(t);
        if (cabeza == null) cabeza = nuevo;
        else { Nodo a = cabeza; while (a.siguiente != null) a = a.siguiente; a.siguiente = nuevo; }
    }
    Ticket buscar(int id) {
        Nodo a = cabeza;
        while (a != null) { if (a.ticket.id == id) return a.ticket; a = a.siguiente; }
        return null;
    }
}

class ColaPendientes {
    Nodo frente, fin;
    void encolar(Ticket t) {
        Nodo nuevo = new Nodo(t);
        if (frente == null) frente = fin = nuevo;
        else { fin.siguiente = nuevo; fin = nuevo; }
    }
    Ticket desencolar() {
        if (frente == null) return null;
        Ticket t = frente.ticket;
        frente = frente.siguiente;
        if (frente == null) fin = null;
        return t;
    }
    Ticket verFrente() { return frente != null ? frente.ticket : null; }
}

public class Main {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        ColaPendientes cola = new ColaPendientes();
        ListaResueltos resueltos = new ListaResueltos();
        int op;

        do {
            System.out.println("\n1. Menú Usuario\n2. Menú Administrador\n3. Salir\nOpción:");
            op = s.nextInt(); s.nextLine();

            if (op == 1) {
                int opU;
                do {
                    System.out.println("\n1. Crear Ticket\n2. Buscar por ID\n3. Volver\nOpción:");
                    opU = s.nextInt(); s.nextLine();
                    if (opU == 1) {
                        System.out.print("Nombre: "); String n = s.nextLine();
                        System.out.print("Descripción: "); String d = s.nextLine();
                        Ticket t = new Ticket(n, d);
                        cola.encolar(t);
                        System.out.println("¡Ticket creado! ID: " + t.id);
                    } else if (opU == 2) {
                        System.out.print("ID a buscar: "); int id = s.nextInt(); s.nextLine();
                        Ticket t = resueltos.buscar(id);
                        System.out.println(t != null ? "\nEncontrado en resueltos:\n" + t : "\nEl ticket está pendiente de solución.");
                    }
                } while (opU != 3);
            } else if (op == 2) {
                int opA;
                do {
                    System.out.println("\n1. Ver frente\n2. Resolver frente\n3. Volver\nOpción:");
                    opA = s.nextInt(); s.nextLine();
                    if (opA == 1) {
                        Ticket t = cola.verFrente();
                        System.out.println(t != null ? "\n" + t : "\nNo hay tickets pendientes.");
                    } else if (opA == 2) {
                        Ticket t = cola.desencolar();
                        if (t != null) {
                            t.resolucion = LocalDateTime.now();
                            resueltos.insertar(t);
                            System.out.println("\n¡Ticket ID " + t.id + " resuelto!");
                        } else System.out.println("\nNo hay pendientes para resolver.");
                    }
                } while (opA != 3);
            }
        } while (op != 3);
        s.close();
    }
}
