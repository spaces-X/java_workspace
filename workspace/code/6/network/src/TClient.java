import java.io.*;
import java.net.*;
import java.util.Properties;

public class TClient { // TClient.java����ɴ����������������ӣ�����ĵ���
	public static void main(String[] args) throws Exception {
		int port = 4321; // Ҳ���Դ������ļ��м�������˿�����.
		String host = "127.0.0.1";
		if (args.length == 2) {
			port = Integer.parseInt(args[1]);
			host = args[0];
		}
		Properties p = new Properties();
		p.load(new FileInputStream("property"));
		host = p.getProperty("host");
		port = Integer.parseInt(p.getProperty("port"));
//		System.out.println("port: "+p.getProperty("port"));
//		System.out.println(Integer.reverse(5));
		Socket s = new Socket(host, port); // ����Socket����
		DataInputStream dis = new DataInputStream(s.getInputStream());
		DataOutputStream dos = new DataOutputStream(s.getOutputStream());
		dos.writeInt(1); // Э������
		dos.writeInt(5); // �����������ݸ�������������ӷ�����
		dos.writeInt(6);
		dos.flush(); // ǿ����������е�����
		System.out.println("5 + 6 = " + dis.readInt()); // �ӷ������˶�����
		dos.writeInt(2); // Э������
		dos.writeUTF("nihao"); // �����ַ�������������
		dos.flush(); // ǿ����������е�����
		System.out.println(dis.readUTF()); // �ӷ������˶�����
		s.close(); // ����ͨ�Ž������ر�Socket����
	}
}