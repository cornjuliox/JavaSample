package controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import model.Message;

/*
 * Please note that this is a simulated message server. Not an actual one.
 * This would be a very poor actual message server.
 * Thanks.
 */

public class MessageServer implements Iterable<Message> {
	private Map<Integer, List<Message>> messages;
	private List<Message> selected;

	public MessageServer() {
		messages = new TreeMap<Integer, List<Message>>();
		selected = new ArrayList<Message>();

		List<Message> list = new ArrayList<Message>();

		list.add(new Message("The cat is missing.", "Have you seen the kitty?"));
		list.add(new Message("Holy cow.", "Maybe in India it is."));
		
		messages.put(0, list);
		
		list = new ArrayList<Message>();
		list.add(new Message("Meetup on July 6", "Everyone is going!"));
		list.add(new Message("Going to the beach",
				"Summer's here and I'd like to have some fun!"));
		
		messages.put(1, list);
	}
	
	public void setSelectedServers(Set<Integer> servers) {
		for(Integer id: servers) {
			if(messages.containsKey(id)) {
				selected.addAll(messages.get(id));
			}
		}
	}
	
	public int getMessageCount() {
		return selected.size();
	}

	@Override
	public Iterator<Message> iterator() {
		// TODO Auto-generated method stub
		return new MessageIterator(selected);
	}
}

class MessageIterator implements Iterator {
	
	private Iterator<Message> iterator;
	
	public MessageIterator(List<Message> messages) {
		iterator = messages.iterator();
	}

	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return iterator.hasNext();
	}

	@Override
	public Object next() {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return iterator.next();
	}

	@Override 
	public void remove() {
		iterator.remove();
	}
	
}