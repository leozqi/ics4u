class Song {
	int bottles;

	public Song(int bottleNum) {
		this.bottles = bottleNum;
	}

	public void lyrics() {
		while (this.bottles > 0) {
			System.out.println("𝅘𝅥𝅮  "
				+ this.bottles
				+ " bottles of beer on the wall 𝅘𝅥𝅮 "
			);
			System.out.println("𝅘𝅥𝅮   "
				+ this.bottles
				+ " bottles of beer! 𝅘𝅥𝅮 "
			);
			System.out.println("𝅘𝅥𝅮  "
				+ "Take one down, pass it around, 𝅘𝅥𝅮 "
			);
			System.out.println("𝅘𝅥𝅮   "
				+ this.bottles
				+ " bottles of beer on the wall 𝅘𝅥𝅮 "
			);
			this.bottles--;
		}
	}
}

public class Sing {
	public static void main(String[] args) {
		Song beer_song = new Song(100);
		beer_song.lyrics();
	}
}
