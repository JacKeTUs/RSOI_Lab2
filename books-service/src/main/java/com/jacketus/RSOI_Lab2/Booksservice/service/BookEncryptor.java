package com.jacketus.RSOI_Lab2.Booksservice.service;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.ArrayList;

public class BookEncryptor {

    public static byte[] generateMaskKey(String hwInfo) {
        byte[] key;
        int timeN = 60;

        long unixTime = System.currentTimeMillis() / 1000L;
        int timeMask = (int)unixTime / timeN;

        //key = SHACryptor.getSHA256(timeMask + " " + hwInfo + userbook_key + "RandomString").substring(0, 16).getBytes();
        key = SHACryptor.getSHA256(timeMask + " " + hwInfo + "RandomString").substring(0, 16).getBytes();
        return key;
    }

    public static byte[] generateBookKey() throws Exception {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        KeySpec spec = new PBEKeySpec("password".toCharArray(), salt, 256, 128); // AES-256
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] key = f.generateSecret(spec).getEncoded();
        return key;
    }


    public static String encryptBook(String HW_INFO, File originFile, File encrypted, byte[] Mkey) {

        String k = "";
        try {
            Files.copy(originFile.toPath(), encrypted.toPath(), StandardCopyOption.REPLACE_EXISTING);
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(encrypted);

            Node documentElement = doc.getDocumentElement();

            ArrayList<Node> queue = new ArrayList<>();
            queue.add(documentElement);

            HW_INFO = "offline";
            byte[] key = generateMaskKey(HW_INFO);

            int test = 0;
            while (queue.size() > 0) {
                Node node = queue.get(0);
                if (node.getChildNodes().getLength() == 0) {
                    test++;
                    node.setTextContent(AESCryptor.encrypt(key, "RandomInitVector", node.getTextContent()));
                } else {
                    NodeList nl = node.getChildNodes();
                    for (int i = 0; i < nl.getLength(); i++) {
                        queue.add(nl.item(i));
                    }
                }
                queue.remove(0);
            }
//            System.out.println("NODES: " + test);
//            // Зашифровываем маскировочным ключом книжный ключ
//            k = AESCryptor.encryptWOUTF8(Mkey, "RandomInitVector", key);

            // Сохраням файл
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(encrypted);
            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Возвращаем замаскированный книжный ключ
        return "ok";
    }


}
