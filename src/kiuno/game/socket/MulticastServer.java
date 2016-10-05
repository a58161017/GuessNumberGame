package kiuno.game.socket;
import java.io.*;

public class MulticastServer {
	public static void main(String[] args) throws IOException {
		new MulticastServerThread().start();
	}
}