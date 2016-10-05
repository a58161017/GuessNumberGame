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
				System.out.println("等待接收一個群播訊息...");
				
				String str = displayPacketDetails(packetFrom);
				if("numgame".equals(str.toLowerCase())){
					isGame = true;
					str = "有人提議玩猜數字遊戲，請開始猜數字：";
					Random rn = new Random();
					num = rn.nextInt(100) + 1;
				}
				if(!"有人提議玩猜數字遊戲，請開始猜數字：".equals(str) && isGame){
					str = guessNumber(str,num);
				}
				
				byte[] msgTo = str.getBytes();
				DatagramPacket packetTo = new DatagramPacket(msgTo, msgTo.length, group, 4446);
				socket.send(packetTo);
				System.out.println("已發送一個訊息在群播上");
				
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
				return str + "超過1~100範圍";
			
			if(num > guess){
				return "比" + str + "要小";
			}else if(num < guess){
				return "比" + str + "要大";
			}else if(num == guess){
				isGame = false;
				return "恭喜你猜中了，答案是:" + str;
			}else{
				return "出錯了";
			}
		}catch(Exception e){
			return str + " 不是數字阿";
		}
	}
}