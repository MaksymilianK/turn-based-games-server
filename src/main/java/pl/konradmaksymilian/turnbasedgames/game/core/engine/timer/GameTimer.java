package pl.konradmaksymilian.turnbasedgames.game.core.engine.timer;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public abstract class GameTimer {
	
	protected Instant currentExecutionStart;
	
	private Instant gameStart;
	
	private ScheduledFuture<?> currentExecution;
		
	private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
	
	public void countdown(Runnable onCountdownEnd) {
		schedule(Duration.ofSeconds(10), onCountdownEnd);
	}
	
	public boolean isRunning() {
		return !currentExecution.isCancelled() && !currentExecution.isDone();
	}
	
	public Instant getCurrentExecutionStart() {
		return currentExecutionStart;
	}
	
	public synchronized boolean stop() {
		if (isRunning()) {
			currentExecution.cancel(true);
			return true;
		} else {
			return false;
		}
	}
	
	public Instant getNow() {
		return Instant.now();
	}
	
	public long getNowEpochMilli() {
		return Instant.now().toEpochMilli();
	}
	
	public Instant getGameStart() {
		return gameStart;
	}
	
	protected synchronized void schedule(Duration time, Runnable onTimeEnd) {
		if (isRunning()) {
			throw new GameTimerException("Cannot schedule a new action if the previous one is not finished");
		}
		
		currentExecution = executor.schedule(onTimeEnd, time.toSeconds(), TimeUnit.SECONDS);
		currentExecutionStart = getNow();
	}
}
