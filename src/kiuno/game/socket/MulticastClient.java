package kiuno.game.socket;
import java.io.*;
import java.net.*;

public class MulticastClient {
	public static void main(String[] args) throws IOException {
		System.out.println("提醒玩家，輸入NumGame可以跟其他人一起玩猜數字遊戲喔!!");
		
		String svAddress = "[server_ip]"; //換成server的IP位置
		int svPort = 12345; //設定伺服器端的port
		
		MulticastSocket socket = new MulticastSocket(4446); //群播port
		InetAddress address = InetAddress.getByName("230.0.0.1"); //群播IP位置
		socket.joinGroup(address);
		
		// 建立一個執行緒，用來接收群播訊息
		Runnable runnable = () -> handleClientRequest(socket);
		new Thread(runnable).start();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String promptMsg = "請輸入一段訊息(Bye to quit):";
		String msg = null;
		System.out.print(promptMsg);
		while ((msg = br.readLine()) != null) {
			if (msg.equalsIgnoreCase("bye")) {
				break;
			}
			
			//發送訊息給伺服器
			byte[] msgTo = msg.getBytes();
			InetAddress server = InetAddress.getByName(svAddress);
			DatagramPacket packet = new DatagramPacket(msgTo, msgTo.length, server, svPort);
			socket.send(packet);
			System.out.println("已將訊息發送給伺服器\n");
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
		
		System.out.println("\n接收到群播傳來的訊息");
		System.out.println("訊息發送來源:[IP Address=" + remoteAddr + ", port=" + remotePort + "]");
		System.out.println("發送內容為:\n" + msg + "\n");
	}
}