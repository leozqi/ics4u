package pong;

public enum Mode {
	PLAYER {
		public String toString() {
			return "Player";
		}
	},
	COMPUTER_SIMPLE {
		public String toString() {
			return "Simple CPU";
		}
	},
	COMPUTER_SMART {
		public String toString() {
			return "Smart CPU";
		}
	}
}
