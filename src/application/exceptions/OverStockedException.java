package application.exceptions;

public class OverStockedException extends RuntimeException {
	public OverStockedException(String message) {
		super(message);
	}
}
