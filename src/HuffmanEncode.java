import javax.swing.*;
import java.io.*;
import java.util.*;
import javax.swing.filechooser.FileNameExtensionFilter;

class Node {
    int freq;
    char c;
    Node left;
    Node right;
    Node(char ch, int i, Node l, Node r) {
        c = ch;
        freq = i;
        left = l;
        right = r;
    }
}

class Compare_Nodes implements Comparator<Node> {
    public int compare(Node x, Node y) {
        return x.freq - y.freq;
    }  
}

public class HuffmanEncode {
    static Scanner input = new Scanner(System.in);
    static File f = null;
    static FileWriter myWriter = null;
    static FileReader reader = null;

    public String encode(String selectedFile) {

        //File selectedFile = fileChooser.getSelectedFile();
        //String filePath = selectedFile.getAbsolutePath();
        String filePath = selectedFile;

        String s = "";
        Map<Character, Integer> freqMap = new HashMap<>();
        String code = "";

        try {
            reader = new FileReader(selectedFile);
            Scanner in = new Scanner(reader);
            while (in.hasNext()) {
                s = s + in.nextLine();
            }
            reader.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Count frequency of each character
        for (char ch : s.toCharArray()) {
            freqMap.put(ch, freqMap.getOrDefault(ch, 0) + 1);
        }

        PriorityQueue<Node> q = new PriorityQueue<>(new Compare_Nodes()); // Min-heap
        for (Map.Entry<Character, Integer> entry : freqMap.entrySet()) {
            q.add(new Node(entry.getKey(), entry.getValue(), null, null));
        }

        // Create Huffman tree
        while (q.size() > 1) {
            Node left = q.poll();
            Node right = q.poll();
            Node newNode = new Node('*', left.freq + right.freq, left, right);
            q.add(newNode);
        }

        Node root = q.peek(); // Root of the Huffman tree

        // Generate Huffman codes
        Map<Character, String> huffmanCodes = new HashMap<>();
        generateHuffmanCodes(root, "", huffmanCodes);

        // ให้ผู้ใช้เลือกที่อยู่ของไฟล์ table ที่จะบันทึก
        JFileChooser saveChooser = new JFileChooser();
        saveChooser.setDialogTitle("Select the output path for .table file");
        saveChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int saveResult = saveChooser.showSaveDialog(null);
        if (saveResult != JFileChooser.APPROVE_OPTION) {
            //System.out.println("Save path selection was cancelled.");
            JOptionPane.showMessageDialog(null, "Save path selection was cancelled.");
//            return;
        }

        // ตรวจสอบว่าไฟล์ที่เลือกมีนามสกุล .table หรือไม่
        File tableFile = saveChooser.getSelectedFile();
        if (!tableFile.getName().endsWith(".table")) {
            tableFile = new File(tableFile.getAbsolutePath() + ".table");
        }

        // เขียนตารางรหัส Huffman ลงไฟล์ .table
        try {
            myWriter = new FileWriter(tableFile);
            for (Map.Entry<Character, String> entry : huffmanCodes.entrySet()) {
                myWriter.write(entry.getKey() + " " + entry.getValue() + "\n");
            }
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Encode the string based on Huffman codes
        for (char ch : s.toCharArray()) {
            code = code + huffmanCodes.get(ch);
        }

        // Convert the code into bytes and write to the output file
        byte[] bytes = new byte[(code.length() + 6) / 7];
        for (int i = 0; i < code.length(); i += 7) {
            String byteStr = code.substring(i, Math.min(i + 7, code.length()));
            while (byteStr.length() < 7) {
                byteStr = "0" + byteStr; // Pad with leading zeros
            }
            bytes[i / 7] = (byte) Integer.parseInt(byteStr, 2);
        }

        // ให้ผู้ใช้เลือกที่อยู่ของไฟล์ .Huffman ที่จะบันทึก
        JFileChooser huffmanSaveChooser = new JFileChooser();
        huffmanSaveChooser.setDialogTitle("Select the output path for .CSC360 file");
        huffmanSaveChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int huffmanSaveResult = huffmanSaveChooser.showSaveDialog(null);
        if (huffmanSaveResult != JFileChooser.APPROVE_OPTION) {
            //System.out.println("Save path selection for .CSC360 was cancelled.");
            JOptionPane.showMessageDialog(null, "Save path selection for .CSC360 was cancelled.");
//            return;
        }

        // ตรวจสอบว่าไฟล์ที่เลือกมีนามสกุล .Huffman หรือไม่
        File huffmanFile = huffmanSaveChooser.getSelectedFile();
        if (!huffmanFile.getName().endsWith(".CSC360")) {
            huffmanFile = new File(huffmanFile.getAbsolutePath() + ".CSC360");
        }

        // เขียนข้อมูลเข้ารหัสลงในไฟล์ .Huffman
        try {
            FileOutputStream fos = new FileOutputStream(huffmanFile);
            fos.write(bytes);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        String differ = showFileSizeDifference(selectedFile, code);
        
        return differ;
    }

//    public String showFileSizeDifference(String selectedFile, String encodedData) {
//        // ขนาดไฟล์ต้นฉบับในหน่วยบิต
//        long originalFileSizeInBits = selectedFile.length() * 8;
//
//        // ขนาดของข้อมูลที่เข้ารหัสในหน่วยบิต
//        long encodedSizeInBits = encodedData.length();
//
//        System.out.println("Original File Size: " + originalFileSizeInBits + " bits");
//        System.out.println("Encoded File Size (in bits): " + encodedSizeInBits + " bits");
//
//        // แสดงเปอร์เซ็นต์การลดขนาด
//        double compressionRatio = ((double)(originalFileSizeInBits - encodedSizeInBits) / originalFileSizeInBits) * 100;
//        System.out.println("Compression Ratio: " + compressionRatio + "%");
//        
//        String formattedCompressionRatio = String.format("%.2f", compressionRatio);
//        
//        String myStr = "Original File Size: %s bits\n" + 
//                        "Encoded File Size (in bits): %s bits\n" +
//                        "Compression Ratio: %s %%";
//
//        String result = String.format(myStr, originalFileSizeInBits, encodedSizeInBits, formattedCompressionRatio);
//        
//        
//        return result;
//    }
    
    public String showFileSizeDifference(String filePath, String encodedData) {
        // สร้าง File object จาก filePath ที่ได้รับ
        File selectedFile = new File(filePath);

        // ขนาดไฟล์ต้นฉบับในหน่วยบิต
        long originalFileSizeInBits = selectedFile.length() * 8;

        // ขนาดของข้อมูลที่เข้ารหัสในหน่วยบิต
        long encodedSizeInBits = encodedData.length();

        System.out.println("Original File Size: " + originalFileSizeInBits + " bits");
        System.out.println("Encoded File Size (in bits): " + encodedSizeInBits + " bits");

        // คำนวณเปอร์เซ็นต์การลดขนาด
        double compressionRatio = ((double)(originalFileSizeInBits - encodedSizeInBits) / originalFileSizeInBits) * 100;
        System.out.println("Compression Ratio: " + compressionRatio + "%");

        // ฟอร์แมต compression ratio เป็นทศนิยม 2 ตำแหน่ง
        String formattedCompressionRatio = String.format("%.2f", compressionRatio);

        // สร้างสตริงผลลัพธ์
        String myStr = "Original File Size: %s bits\n" + 
                       "Encoded File Size (in bits): %s bits\n" +
                       "Compression Ratio: %s %%";

        // ฟอร์แมตค่าและคืนค่าผลลัพธ์
        String result = String.format(myStr, originalFileSizeInBits, encodedSizeInBits, formattedCompressionRatio);

        return result;
    }
    
    public String decode(String selectedFile) {

//        JFileChooser fileChooser = new JFileChooser();
//        fileChooser.setDialogTitle("Select the .CSC360 file to decode");
//        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
//        int result = fileChooser.showOpenDialog(null);
//        if (result != JFileChooser.APPROVE_OPTION) {
//            System.out.println("File selection was cancelled.");
//            //return;
//        }
//
//        File selectedFile = fileChooser.getSelectedFile();
//        String filePath = selectedFile.getAbsolutePath();
        String filePath = selectedFile;
        
        
        String code = "";
        try {
            FileInputStream is = new FileInputStream(selectedFile);
            byte[] b = is.readAllBytes();
            is.close();

            for (int i = 0; i < b.length - 1; i++) {
                code += String.format("%7s", Integer.toBinaryString(b[i] & 0xFF)).replace(' ', '0');
            }

            int len = b[b.length - 1];
            code += String.format("%7s", Integer.toBinaryString(b[b.length - 2] & 0xFF)).replace(' ', '0');
            code = code.substring(0, code.length() - len);

        } catch (IOException e) {
            e.printStackTrace();
        }

        Node root = new Node('*', 0, null, null);

        JFileChooser tableFileChooser = new JFileChooser();
        tableFileChooser.setDialogTitle("Select the .table file for decoding");
        tableFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int tableResult = tableFileChooser.showOpenDialog(null);
        if (tableResult != JFileChooser.APPROVE_OPTION) {
            //System.out.println("File selection for .table was cancelled.");
            JOptionPane.showMessageDialog(null, "File selection for .table was cancelled.");
            //return;
        }

        File tableFile = tableFileChooser.getSelectedFile();
        try {
            reader = new FileReader(tableFile);
            Scanner in = new Scanner(reader);
            while (in.hasNext()) {
                String st = in.nextLine();
                char ch = st.charAt(0);
                String temp = st.substring(2);
                createTree(root, temp, ch);
            }
            reader.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder decodedMessage = new StringBuilder();
        Node currentNode = root;
        for (int i = 0; i < code.length(); i++) {
            if (code.charAt(i) == '0') {
                currentNode = currentNode.left;
            } else {
                currentNode = currentNode.right;
            }
            if (currentNode.left == null && currentNode.right == null) {
                decodedMessage.append(currentNode.c);
                currentNode = root;
            }
        }

        JFileChooser saveChooser = new JFileChooser();
        saveChooser.setDialogTitle("Save decoded message to file");
        int saveResult = saveChooser.showSaveDialog(null);
        if (saveResult != JFileChooser.APPROVE_OPTION) {
            //System.out.println("Save path selection was cancelled.");
            JOptionPane.showMessageDialog(null, "Save path selection was cancelled.");
            //return;
        }

        File outputFile = saveChooser.getSelectedFile();
        writeToFile(outputFile, decodedMessage.toString());
        
        String differ = decodedMessage.toString();
        
        return differ;
    }

    public static void writeToFile(File outputFile, String content) {
        try {

            String filePath = outputFile.getAbsolutePath();
            if (!filePath.endsWith(".txt")) {

                outputFile = new File(filePath + ".txt");
            }

            FileWriter writer = new FileWriter(outputFile);
            writer.write(content);
            writer.close();
            //System.out.println("File saved successfully to: " + outputFile.getAbsolutePath());
            JOptionPane.showMessageDialog(null, "File saved successfully to: " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void createTree(Node root, String str, char ch) {
        if (str.length() == 1) {
            if (str.charAt(0) == '0') {
                root.left = new Node(ch, 0, null, null);
            } else {
                root.right = new Node(ch, 0, null, null);
            }
            return;
        }

        if (str.charAt(0) == '0') {
            if (root.left == null) root.left = new Node('*', 0, null, null);
            createTree(root.left, str.substring(1), ch);
        } else {
            if (root.right == null) root.right = new Node('*', 0, null, null);
            createTree(root.right, str.substring(1), ch);
        }
    }

    public static void generateHuffmanCodes(Node root, String s, Map<Character, String> huffmanCodes) {
        if (root.left == null && root.right == null && root.c != '*') {
            huffmanCodes.put(root.c, s);
            return;
        }
        generateHuffmanCodes(root.left, s + "0", huffmanCodes);
        generateHuffmanCodes(root.right, s + "1", huffmanCodes);
    }
}
