import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

// Kelas abstrak MenuItem
abstract class MenuItem {
    private String nama;
    private double harga;
    private String kategori;

    public MenuItem(String nama, double harga, String kategori) {
        this.nama = nama;
        this.harga = harga;
        this.kategori = kategori;
    }

    public String getNama() {
        return nama;
    }

    public double getHarga() {
        return harga;
    }

    public String getKategori() {
        return kategori;
    }

    public abstract void tampilMenu();
}

// Kelas Makanan
class Makanan extends MenuItem {
    private String jenisMakanan;

    public Makanan(String nama, double harga, String kategori, String jenisMakanan) {
        super(nama, harga, kategori);
        this.jenisMakanan = jenisMakanan;
    }

    @Override
    public void tampilMenu() {
        System.out.println("Makanan: " + getNama() + ", Harga: " + getHarga() + ", Jenis: " + jenisMakanan);
    }
}

// Kelas Minuman
class Minuman extends MenuItem {
    private String jenisMinuman;

    public Minuman(String nama, double harga, String kategori, String jenisMinuman) {
        super(nama, harga, kategori);
        this.jenisMinuman = jenisMinuman;
    }

    @Override
    public void tampilMenu() {
        System.out.println("Minuman: " + getNama() + ", Harga: " + getHarga() + ", Jenis: " + jenisMinuman);
    }
}

// Kelas Diskon
class Diskon extends MenuItem {
    private double diskon;

    public Diskon(String nama, double harga, String kategori, double diskon) {
        super(nama, harga, kategori);
        this.diskon = diskon;
    }

    public double getDiskon() {
        return diskon;
    }

    @Override
    public void tampilMenu() {
        System.out.println("Diskon: " + getNama() + ", Harga: " + getHarga() + ", Diskon: " + diskon + "%");
    }
}

// Kelas Menu
class Menu {
    private ArrayList<MenuItem> items = new ArrayList<>();

    public void tambahItem(MenuItem item) {
        items.add(item);
    }

    public void tampilMenu() {
        for (MenuItem item : items) {
            item.tampilMenu();
        }
    }

    public MenuItem getItemByName(String nama) throws Exception {
        for (MenuItem item : items) {
            if (item.getNama().equalsIgnoreCase(nama)) {
                return item;
            }
        }
        throw new Exception("Item tidak ditemukan");
    }

    public void simpanMenu(String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (MenuItem item : items) {
                writer.write(item.getKategori() + ";" + item.getNama() + ";" + item.getHarga() + "\n");
            }
        }
    }

    public void muatMenu(String fileName) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                String kategori = parts[0];
                String nama = parts[1];
                double harga = Double.parseDouble(parts[2]);
                if (kategori.equalsIgnoreCase("Makanan")) {
                    tambahItem(new Makanan(nama, harga, kategori, ""));
                } else if (kategori.equalsIgnoreCase("Minuman")) {
                    tambahItem(new Minuman(nama, harga, kategori, ""));
                } else if (kategori.equalsIgnoreCase("Diskon")) {
                    tambahItem(new Diskon(nama, harga, kategori, harga));
                }
            }
        }
    }
}

// Kelas Pesanan
class Pesanan {
    private ArrayList<MenuItem> itemsDipesan = new ArrayList<>();

    public void tambahItem(MenuItem item) {
        itemsDipesan.add(item);
    }

    public double hitungTotal() {
        double total = 0;
        for (MenuItem item : itemsDipesan) {
            total += item.getHarga();
        }
        for (MenuItem item : itemsDipesan) {
            if (item instanceof Diskon) {
                total -= ((Diskon) item).getDiskon() / 100 * total;
            }
        }
        return total;
    }

    public void tampilkanStruk() {
        for (MenuItem item : itemsDipesan) {
            item.tampilMenu();
        }
        System.out.println("Total: " + hitungTotal());
    }

    public void simpanStruk(String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (MenuItem item : itemsDipesan) {
                writer.write(item.getNama() + ";" + item.getHarga() + "\n");
            }
            writer.write("Total: " + hitungTotal() + "\n");
        }
    }
}

// Main class untuk mengelola interaksi
public class Main {
    private static Menu menu = new Menu();
    private static Pesanan pesanan = new Pesanan();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("Menu Utama:");
            System.out.println("1. Tambah Item Baru ke Menu");
            System.out.println("2. Tampilkan Menu Restoran");
            System.out.println("3. Terima Pesanan dari Pelanggan");
            System.out.println("4. Hitung Total Biaya Pesanan");
            System.out.println("5. Tampilkan Struk Pesanan Pelanggan");
            System.out.println("6. Keluar");
            System.out.print("Pilih opsi: ");
            int pilihan = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            try {
                switch (pilihan) {
                    case 1:
                        tambahItemMenu();
                        break;
                    case 2:
                        menu.tampilMenu();
                        break;
                    case 3:
                        terimaPesanan();
                        break;
                    case 4:
                        System.out.println("Total biaya: " + pesanan.hitungTotal());
                        break;
                    case 5:
                        pesanan.tampilkanStruk();
                        break;
                    case 6:
                        System.out.println("Keluar dari program...");
                        return;
                    default:
                        System.out.println("Pilihan tidak valid.");
                }
            } catch (Exception e) {
                System.out.println("Terjadi kesalahan: " + e.getMessage());
            }
        }
    }

    private static void tambahItemMenu() throws IOException {
        System.out.print("Masukkan kategori (Makanan/Minuman/Diskon): ");
        String kategori = scanner.nextLine();
        System.out.print("Masukkan nama: ");
        String nama = scanner.nextLine();
        System.out.print("Masukkan harga: ");
        double harga = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        switch (kategori.toLowerCase()) {
            case "makanan":
                System.out.print("Masukkan jenis makanan: ");
                String jenisMakanan = scanner.nextLine();
                menu.tambahItem(new Makanan(nama, harga, kategori, jenisMakanan));
                break;
            case "minuman":
                System.out.print("Masukkan jenis minuman: ");
                String jenisMinuman = scanner.nextLine();
                menu.tambahItem(new Minuman(nama, harga, kategori, jenisMinuman));
                break;
            case "diskon":
                System.out.print("Masukkan persentase diskon: ");
                double diskon = scanner.nextDouble();
                scanner.nextLine(); // Consume newline
                menu.tambahItem(new Diskon(nama, harga, kategori, diskon));
                break;
            default:
                System.out.println("Kategori tidak valid.");
        }
    }

    private static void terimaPesanan() throws Exception {
        System.out.print("Masukkan nama item yang dipesan: ");
        String nama = scanner.nextLine();
        MenuItem item = menu.getItemByName(nama);
        pesanan.tambahItem(item);
    }
}
