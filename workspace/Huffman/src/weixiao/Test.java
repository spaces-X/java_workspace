package weixiao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.function.BinaryOperator;

public class Test {
	/* ����ͳ���ַ����ֵĴ��� ����ӳ���ϵmap<Character,Integer> */
	public static Map<Character, Integer> statistics(char[] charArray) {  //ͳ�Ƴ��ֵ�Ƶ��
		Map<Character, Integer> map = new HashMap<Character, Integer>();  
        for (char c : charArray) {  
            Character character = new Character(c);  
            if (map.containsKey(character)) {  
                map.put(character, map.get(character) + 1);  
            } else {  
                map.put(character, 1);  
            }  
        }  
  
        return map;  
    }  
	/* ����ͳ�Ƴ���Map �� �޸�leafs */
	private static Tree buildTree(Map<Character, Integer> statistics,  
            List<Node> leafs) {  
        Character[] keys = statistics.keySet().toArray(new Character[0]);  
  
        PriorityQueue<Node> priorityQueue = new PriorityQueue<Node>();  //���ȼ����� ʵ��Node�д�frequency����β��
        for (Character character : keys) {                              //���ν���
            Node node = new Node();  
            node.chars = character.toString();  
            node.frequence = statistics.get(character);  
            priorityQueue.add(node);  
            leafs.add(node);  
        }  
  
        int size = priorityQueue.size();  
        for (int i = 1; i <= size - 1; i++) {  
            Node node1 = priorityQueue.poll();  
            Node node2 = priorityQueue.poll();  
  
            Node sumNode = new Node();                          //�Ͻڵ�
            sumNode.chars = node1.chars + node2.chars;  
            sumNode.frequence = node1.frequence + node2.frequence;  
  
            sumNode.leftNode = node1;  
            sumNode.rightNode = node2;  
  
            node1.parent = sumNode;  
            node2.parent = sumNode;  
  
            priorityQueue.add(sumNode);  
        }  
  
        Tree tree = new Tree();  
        tree.root = priorityQueue.poll();  
        return tree;  
    }  
	/* ʵ�ֱ��� */
	public static String encode(String originalStr,  
            Map<Character, Integer> statistics) {  
        if (originalStr == null || originalStr.equals("")) {  
            return "";  
        }  
  
        char[] charArray = originalStr.toCharArray();
        List<Node> leafNodes = new ArrayList<Node>();  
        buildTree(statistics, leafNodes);  
        Map<Character, String> encodInfo = buildEncodingInfo(leafNodes);  
  
        StringBuffer buffer = new StringBuffer();
        for (char c : charArray) {  
            Character character = new Character(c);
            buffer.append(encodInfo.get(character));
        }  
  
        return buffer.toString();  
    } 
	/* ������������������е�ӳ��*/
	private static Map<Character, String> buildEncodingInfo(List<Node> leafNodes) {  
        Map<Character, String> codewords = new HashMap<Character, String>(); //���� ��Ӧ�Ķ��������� 
        for (Node leafNode : leafNodes) {  
            Character character = new Character(leafNode.getChars().charAt(0));  
            String codeword = "";  
            Node currentNode = leafNode;  
  
            do {  
                if (currentNode.isLeftChild()) {  //��0��1
                    codeword = "0" + codeword;  
                } else {  
                    codeword = "1" + codeword;  
                }  
  
                currentNode = currentNode.parent;  
            } while (currentNode.parent != null);  
  
            codewords.put(character, codeword);  //�����ֺͶ�Ӧ�Ķ��������м��뵽map��
        }  
  
        return codewords;  //����ӳ���б�
    }  
	/* ���� */
	public static String decode(String binaryStr,  
            Map<Character, Integer> statistics) {  
        if (binaryStr == null || binaryStr.equals("")) {  
            return null;  
        }  
  
        char[] binaryCharArray = binaryStr.toCharArray();  
        LinkedList<Character> binaryList = new LinkedList<Character>();  
        int size = binaryCharArray.length;  
        for (int i = 0; i < size; i++) 
        {  
            binaryList.addLast(new Character(binaryCharArray[i]));  
        }  
  
        List<Node> leafNodes = new ArrayList<Node>();  
        Tree tree = buildTree(statistics, leafNodes);  
  
        StringBuffer buffer = new StringBuffer();  
  
        while (binaryList.size() > 0) {  
            Node node = tree.root;  
  
            do {  
                Character c = binaryList.removeFirst();  
                if (c.charValue() == '0') {  
                    node = node.leftNode;  
                } else {  
                    node = node.rightNode;  
                }  
            } while (!node.isLeaf());  
  
            buffer.append(node.chars);  
        }  
  
        return buffer.toString();  
    }  
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		 	BufferedReader br=new BufferedReader(new FileReader("weixiao.txt"));
		 	String oriStr = "";
		 	String sum="";
		 	float length1=0;
		 	float length2=0;
		 	Map<Character, Integer> statistics=null;
		 	BitWriter bw=new BitWriter("huffman.zip");
		 	while((oriStr=br.readLine())!=null)
		 	{
		        sum+=(oriStr+"\n");
		 	}
		 	statistics = statistics(sum.toCharArray()); 
		 	br = new BufferedReader(new FileReader("weixiao.txt"));
		 	while((oriStr=br.readLine())!=null)
		 	{
		 		oriStr+="\n";
		        String encodedBinariStr = encode(oriStr, statistics);  
		        System.out.println(encodedBinariStr);
		        for(int i=0;i<encodedBinariStr.length();i++)
		        {
		        	bw.writeBit(Integer.parseInt(encodedBinariStr.substring(i, i+1)));
		        }
		        length1 += encodedBinariStr.length();
		        length2 += getStringOfByte(oriStr, Charset.forName("UTF-8")).length();
		        
		 	}
		 	bw.close();
		 	
		 	BitReader bitReader = new BitReader("huffman.zip");
		 	String binary="";
		 	while(bitReader.hasNext()){
				if(bitReader.next())
					binary+=1+"";
				else
					binary+=0+"";
		 	}
		 	System.out.println("huffman��"+binary);
		 	System.out.println("UTF��8��"+getStringOfByte(sum,Charset.forName("UTF-8")));
		 	System.out.println("ѹ����Ϊ��"+((float)(length1/length2))*100+"%");
		 	String decodedStr = decode(binary, statistics);
		 	System.out.println(decodedStr);
		 	String newFileName="decode.txt";
		 	if(args.length>2)
		 	{
		 		newFileName=args[1];
		 	}
		 	FileWriter fw = new FileWriter(newFileName);
		 	fw.write(decodedStr);
		 	fw.flush();
		 	fw.close();

	}
	  
	    public static String getStringOfByte(String str, Charset charset) {  
	        if (str == null || str.equals("")) {  
	            return "";  
	        }  
	  
	        byte[] byteArray = str.getBytes(charset);  
	        int size = byteArray.length;  
	        StringBuffer buffer = new StringBuffer();  
	        for (int i = 0; i < size; i++) {  
	            byte temp = byteArray[i];  
	            buffer.append(getStringOfByte(temp));  
	        }  
	  
	        return buffer.toString();  
	    }  
	  
	    public static String getStringOfByte(byte b) {  
	        StringBuffer buffer = new StringBuffer();  
	        for (int i = 7; i >= 0; i--) {  
	            byte temp = (byte) ((b >> i) & 0x1);  
	            buffer.append(String.valueOf(temp));  
	        }  
	  
	        return buffer.toString();  
	    }  
	}
