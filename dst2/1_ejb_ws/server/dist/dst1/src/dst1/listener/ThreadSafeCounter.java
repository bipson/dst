package dst1.listener;


public class ThreadSafeCounter {
	
	private int atomicValue = 0;

	public synchronized void increaseMe() {

		atomicValue += 1;

	}
	
	public synchronized void decreaseMe() {

		atomicValue -= 1;

	}

	public synchronized int getMe() {

		return atomicValue;

	}
	
	public synchronized void add(int a) {
		
		atomicValue += a;
		
	}

}

