import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class DeteksiURL {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Program Deteksi Kelas IP, Net ID, Host ID, dan Biner ===");
        System.out.print("Masukkan URL (contoh: www.polinema.ac.id): ");
        String urlInput = scanner.nextLine();

        // Menghapus "http://" atau "https://" jika user tidak sengaja memasukkannya
        String host = urlInput.replaceFirst("^(http[s]?://www\\.|http[s]?://|www\\.)", "");

        try {
            // 1. Mendapatkan IP Address dari URL
            InetAddress inetAddress = InetAddress.getByName(host);
            String ipAddress = inetAddress.getHostAddress();
            
            System.out.println("\n--------------------------------------------------");
            System.out.println("URL Input  : " + urlInput);
            System.out.println("IP Address : " + ipAddress);
            System.out.println("--------------------------------------------------");

            // Memecah IP menjadi array string (4 blok/oktet)
            String[] ipBlocks = ipAddress.split("\\.");
            
            // Mengambil oktet pertama untuk menentukan kelas IP
            int firstOctet = Integer.parseInt(ipBlocks[0]);

            String ipClass = "";
            String netId = "";
            String hostId = "";

            // 2. Menentukan Kelas, Net ID, dan Host ID
            if (firstOctet >= 1 && firstOctet <= 126) {
                ipClass = "A";
                netId = ipBlocks[0];
                hostId = ipBlocks[1] + "." + ipBlocks[2] + "." + ipBlocks[3];
            } else if (firstOctet >= 128 && firstOctet <= 191) {
                ipClass = "B";
                netId = ipBlocks[0] + "." + ipBlocks[1];
                hostId = ipBlocks[2] + "." + ipBlocks[3];
            } else if (firstOctet >= 192 && firstOctet <= 223) {
                ipClass = "C";
                netId = ipBlocks[0] + "." + ipBlocks[1] + "." + ipBlocks[2];
                hostId = ipBlocks[3];
            } else if (firstOctet >= 224 && firstOctet <= 239) {
                ipClass = "D (Multicast)";
                netId = "Tidak ada spesifikasi Net ID (Multicast)";
                hostId = "Tidak ada spesifikasi Host ID (Multicast)";
            } else if (firstOctet >= 240 && firstOctet <= 255) {
                ipClass = "E (Eksperimental)";
                netId = "Tidak ada spesifikasi Net ID";
                hostId = "Tidak ada spesifikasi Host ID";
            } else {
                ipClass = "Loopback / Invalid";
            }

            System.out.println("Kelas IP   : " + ipClass);
            if (!ipClass.contains("D") && !ipClass.contains("E") && !ipClass.contains("Loopback")) {
                System.out.println("Net ID     : " + netId);
                System.out.println("Host ID    : " + hostId);
            }

            // 3. Konversi Biner tiap blok
            System.out.println("--------------------------------------------------");
            System.out.println("Konversi Biner tiap blok:");
            
            StringBuilder fullBinary = new StringBuilder();
            for (int i = 0; i < ipBlocks.length; i++) {
                int blockValue = Integer.parseInt(ipBlocks[i]);
                // Mengubah integer ke biner dan menambahkan padding nol di depan agar selalu 8-bit
                String binaryString = String.format("%8s", Integer.toBinaryString(blockValue)).replace(' ', '0');
                
                System.out.println("Blok " + (i + 1) + " (" + ipBlocks[i] + ")\t: " + binaryString);
                
                fullBinary.append(binaryString);
                if (i < ipBlocks.length - 1) {
                    fullBinary.append(".");
                }
            }
            
            System.out.println("Format Lengkap : " + fullBinary.toString());
            System.out.println("--------------------------------------------------");

        } catch (UnknownHostException e) {
            System.out.println("\nError: URL tidak ditemukan atau koneksi internet bermasalah.");
            System.out.println("Pastikan Anda terkoneksi internet dan URL ditulis dengan benar.");
        }
        
        scanner.close();
    }
}