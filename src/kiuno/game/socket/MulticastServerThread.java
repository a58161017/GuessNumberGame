package kiuno.game.socket;
import java.io.*;
import java.net.*;
import java.util.*;

public class MulticastServerThread extends ServerThread {
	boolean isGame = false;
	int num = 0;

	private long FIVE_SECONDS = 5000;

	public MulticastServerThread() throws IOException {
		super("MulticastServerThread");
	}

	public void run() {
		while (moreQuotes) {
			try {
				byte[] msgFrom = new byte[1024];
				InetAddress group = InetAddress.getByName("230.0.0.1");
				DatagramPacket packetFrom = new DatagramPacket(msgFrom, msgFrom.length);
				socket.receive(packetFrom);
				System.out.println("���ݱ����@�Ӹs���T��...");
				
				String str = displayPacketDetails(packetFrom);
				if("numgame".equals(str.toLowerCase())){
					isGame = true;
					str = "���H��ĳ���q�Ʀr�C���A�ж}�l�q�Ʀr�G";
					Random rn = new Random();
					num = rn.nextInt(100) + 1;
				}
				if(!"���H��ĳ���q�Ʀr�C���A�ж}�l�q�Ʀr�G".equals(str) && isGame){
					str = guessNumber(str,num);
				}
				
				byte[] msgTo = str.getBytes();
				DatagramPacket packetTo = new DatagramPacket(msgTo, msgTo.length, group, 4446);
				socket.send(packetTo);
				System.out.println("�w�o�e�@�ӰT���b�s���W");
				
				try {
					sleep((long) (Math.random() * FIVE_SECONDS));
				} catch (InterruptedException e) {}
			} catch (IOException e) {
				e.printStackTrace();
				moreQuotes = false;
			}
		}
		socket.close();
	}
	
	public String guessNumber(String str, int guess){
		try{
			int num = Integer.parseInt(str);
			if(num < 1 || num > 100)
				return str + "�W�L1~100�d��";
			
			if(num > guess){
				return "��" + str + "�n�p";
			}else if(num < guess){
				return "��" + str + "�n�j";
			}else if(num == guess){
				isGame = false;
				return "���ߧA�q���F�A���׬O:" + str;
			}else{
				return "�X���F";
			}
		}catch(Exception e){
			return str + " ���O�Ʀr��";
		}
	}
}