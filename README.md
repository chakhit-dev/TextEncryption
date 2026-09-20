# 🔒 TextEncryption & Compression (Huffman Coding)

A Java desktop application built with **Java Swing** that provides lossless text compression, encoding, and decoding using the **Huffman Coding** algorithm.

---

## 📌 Features

- **Huffman Text Compression:** Encodes arbitrary text files into compact binary representation using character frequency prefix trees.
- **Decoding & Reconstruction:** Rebuilds and restores original plaintext from encoded data.
- **Graphical User Interface (GUI):** Simple and intuitive Java Swing interface with dedicated frames for encoding (`EncodeFrame`) and decoding (`DecodeFrame`).
- **Ant Build Support:** Ready to build and run via standard NetBeans Ant configurations or terminal commands.

---

## 🛠️ Tech Stack

- **Language:** Java (JDK 8+)
- **GUI Framework:** Java Swing / AWT
- **Algorithm:** Huffman Coding (Greedy, Prefix Trees, Frequency Tables)
- **Build System:** Apache Ant / NetBeans Project Structure

---

## 📂 Project Structure

```text
TextEncryption/
├── nbproject/                 # NetBeans project configuration
├── src/
│   ├── HuffmanEncode.java     # Core Huffman tree construction & bitwise encoding logic
│   ├── EncodeFrame.java       # GUI window for compressing/encoding text
│   ├── EncodeFrame.form       # Swing GUI form definition
│   ├── DecodeFrame.java       # GUI window for decompressing/decoding text
│   └── DecodeFrame.form       # Swing GUI form definition
├── build.xml                  # Apache Ant build script
└── manifest.mf                # Application manifest
