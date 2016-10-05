package kiuno.game.socket;
import java.io.*;
import java.net.*;

public class MulticastClient {
	public static void main(String[] args) throws IOException {
		System.out.println("�������a�A��JNumGame�i�H���L�H�@�_���q�Ʀr�C����!!");
		
		String svAddress = "[server_ip]"; //����server��IP��m
		int svPort = 12345; //�]�w���A���ݪ�port
		
		MulticastSocket socket = new MulticastSocket(4446); //�s��port
		InetAddress address = InetAddress.getByName("230.0.0.1"); //�s��IP��m
		socket.joinGroup(address);
		
		// �إߤ@�Ӱ�����A�Ψӱ����s���T��
		Runnable runnable = () -> handleClientRequest(socket);
		new Thread(runnable).start();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String promptMsg = "�п�J�@�q�T��(Bye to quit):";
		String msg = null;
		System.out.print(promptMsg);
		while ((msg = br.readLine()) != null) {
			if (msg.equalsIgnoreCase("bye")) {
				break;
			}
			
			//�o�e�T�������A��
			byte[] msgTo = msg.getBytes();
			InetAddress server = InetAddress.getByName(svAddress);
			DatagramPacket packet = new DatagramPacket(msgTo, msgTo.length, server, svPort);
			socket.send(packet);
			System.out.println("�w�N�T���o�e�����A��\n");
			System.out.print(promptMsg);
		}
		socket.leaveGroup(address);
		socket.close();
	}

	public static void handleClientRequest(MulticastSocket socket){
		try {
			while (true) {
				byte[] msgFrom = new byte[1024];
				DatagramPacket packet = new DatagramPacket(msgFrom, msgFrom.length);
				socket.receive(packet);
				displayPacketDetails(packet);
			}
		} catch (IOException e) {
		}
	}

	public static void displayPacketDetails(DatagramPacket packet) {
		byte[] msgBuffer = packet.getData();
		int length = packet.getLength();
		int offset = packet.getOffset();
		
		int remotePort = packet.getPort();
		InetAddress remoteAddr = packet.getAddress();
		String msg = new String(msgBuffer, offset, length);
		
		System.out.println("\n������s���ǨӪ��T��");
		System.out.println("�T���o�e�ӷ�:[IP Address=" + remoteAddr + ", port=" + remotePort + "]");
		System.out.println("�o�e���e��:\n" + msg + "\n");
	}
}