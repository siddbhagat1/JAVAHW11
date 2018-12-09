package New;

import java.util.LinkedList;
import java.util.Scanner;
import java.util.Queue;

class Prod implements Runnable{
	Thread t;
	BF bf;
	Prod(BF b){
		t = new Thread(this);
		bf = b;
		t.start();
	}
	public void run() {
		int item = 0;

		// it will keep producing
		while(true) {
			bf.write(item++);
			try {
				Thread.sleep(1000);
			}
			catch(Exception e) {
			}
		}
	}
}

class Cons implements Runnable{
	Thread t;
	BF bf;
	Cons(BF b){
		t = new Thread(this);
		bf = b;
		t.start();
	}
	public void run() {
		//it keep consuming
		while(true) {
			bf.read();
			try {
				Thread.sleep(2000);
			}
			catch(Exception e) {

			}
		}
	}
}

//BF class containing queue as buffer.
class BF{
	Queue<Integer> q;
	int size;

	BF(int size){
		q = new LinkedList<>();
		this.size = size;

	}

	//method which producer will call
	synchronized void write(int item) {

		//if BF is full, wait for consumer to consume.
		while(q.size()==size) {
			try {
				wait();
			}
			catch(InterruptedException e) {
				System.out.println("It is Interrupted");
			}
		}
		q.add(item++);
		System.out.println("Producer produced item : " + (item-1));
		System.out.println("Buffer has: "+q);

		//if BF was empty before, then notify consumer that it has entry now.
		if(q.size() == 1)
			notifyAll();

	}

	//method which consumer will call
	synchronized void read()
	{
		//if BF is empty, wait for producer to produce
		while(q.size()==0)
		{
			try {
				wait();
			}
			catch (InterruptedException e)
			{
				System.out.println("It is Interrupted");
			}
		}
		System.out.println("Consumer consumed item " + q.remove());
		System.out.println("Buffer has: "+q);

		//if BF was full before, then notify producer that it can produce now.
		if(q.size()==size-1){
			notifyAll();
		}


	}

}
public class ProdCon{
	public static void main(String [] args)  {
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter the size of the buffer: ");
		int bSize = sc.nextInt();
		BF bf = new BF(bSize);
		sc.close();

		
		new Prod(bf);
		new Cons(bf);

		//Enter Ctrl-C or click on red square in eclipse to stop
	}

}
