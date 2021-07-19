package fr.darkvodou.NKtrader.managers;


import fr.darkvodou.NKtrader.NKtrader;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;

public class QueueManager
{
	private Queue<Function> queue = null;
	private BukkitRunnable runnableAsync = null;

	public QueueManager()
	{
		this.queue = new ConcurrentLinkedQueue<Function>();
		runnableAsync = new BukkitRunnable()
		{
			@Override public void run()
			{

			}
		};
		runnableAsync.runTask(NKtrader.getPlugin());
	}

	public void addToQueue(Function async)
	{
		this.queue.add(async);
		apply();
	}

	public void apply()
	{
		// If runnableAsync isn't running
		if(!Bukkit.getScheduler().isCurrentlyRunning(runnableAsync.getTaskId()))
		{
			// run it async
			runnableAsync = new BukkitRunnable()
			{
				@Override public void run()
				{
					while(!queue.isEmpty())
					{
						// Get NKFunction
						Function executer = queue.poll();
						// Run async tasks and get a result
						executer.apply(null);
					}
				}
			};
			runnableAsync.runTaskAsynchronously(NKtrader.getPlugin());
		}
	}
}
