/*
    Synthia, a data structure generator
    Copyright (C) 2019-2021 Laboratoire d'informatique formelle
    Université du Québec à Chicoutimi, Canada

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ca.uqac.lif.synthia.sequence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ca.uqac.lif.synthia.relative.PickSmallerComparable;
import ca.uqac.lif.petitpoucet.ComposedPart;
import ca.uqac.lif.petitpoucet.NodeFactory;
import ca.uqac.lif.petitpoucet.Part;
import ca.uqac.lif.petitpoucet.PartNode;
import ca.uqac.lif.petitpoucet.function.ExplanationQueryable;
import ca.uqac.lif.petitpoucet.function.vector.NthElement;
import ca.uqac.lif.synthia.Bounded;
import ca.uqac.lif.synthia.NoMoreElementException;
import ca.uqac.lif.synthia.Picker;
import ca.uqac.lif.synthia.SequenceShrinkable;
import ca.uqac.lif.synthia.Shrinkable;
import ca.uqac.lif.synthia.explanation.NthSuccessiveOutput;
import ca.uqac.lif.synthia.random.RandomFloat;

//TODO check constructors whit a list as parameter for m_values
/**
 * Picker that returns values taken from a list. As its name implies,
 * <tt>Playback</tt> literally replays the values fetched from a list that
 * was passed to its constructor. It loops back to the beginning when the list
 * is over (this can optionally be disabled).
 * <pre>
 * Playback&lt;Integer&gt; p = new Playback&lt;Integer&gt;(2, 7, 1);
 * System.out.println(p.pick()); // 2
 * System.out.println(p.pick()); // 7
 * System.out.println(p.pick()); // 1
 * System.out.println(p.pick()); // 2
 * ...</pre>
 * Optionally, the picker can be told to start at a different element than the
 * first one. By default, the picker keeps outputting the last value of the
 * sequence if more values are requested and the <tt>loop</tt> parameter is
 * set to false.
 * @param <T> The type of objects to return
 * @ingroup API
 */
public class Playback<T> implements Bounded<T>, Shrinkable<T>, ExplanationQueryable, SequenceShrinkable<T>
{
	/**
	 * The values to play back
	 */
	/*@ non_null @*/ protected List<T> m_values;

	/**
	 * The index of the current value
	 */
	protected int m_index;

	/**
	 * The start index
	 */
	protected int m_startIndex;

	/**
	 * Whether to loop through the values
	 */
	protected boolean m_loop;
	
	/**
	 * An optional reference to a {@link Shrinkable} object at the origin of the
	 * array of values to play back.
	 */
	/*@ null @*/ protected Shrinkable<List<T>> m_shrinkable;
	
	/**
	 * Creates a new Playback picker
	 * @param shrinkable A reference to a {@link Shrinkable} object at the origin of the
	 * array of values to play back. Can be null.
	 * @param start_index The position of the first value to return
	 * @param values The list of values to play back
	 */
	public Playback(/*@ null @*/ Shrinkable<List<T>> shrinkable, int start_index, /*@ non_null @*/ List<T> values)
	{
		m_shrinkable = shrinkable;
		m_values = values;
		m_index = start_index;
		m_startIndex = start_index;
		m_loop = true;
	}
	
	/**
	 * Creates a new Playback picker
	 * @param shrinkable A reference to a {@link Shrinkable} object at the origin of the
	 * array of values to play back. Can be null.
	 * @param start_index The position of the first value to return
	 * @param values The values to play back
	 */
	@SuppressWarnings("unchecked")
	public Playback(/*@ null @*/ Shrinkable<List<T>> shrinkable, int start_index, /*@ non_null @*/ T ... values)
	{
		this(shrinkable, start_index, Arrays.asList(values));
	}
	
	/**
	 * Creates a new Playback picker
	 * @param start_index The position of the first value to return
	 * @param values The values to play back
	 */
	@SuppressWarnings("unchecked")
	public Playback(int start_index, /*@ non_null @*/ T ... values)
	{
		this(null, start_index, values);
	}

	/**
	 * Creates a new Playback picker
	 * @param values The values to play back
	 */
	@SuppressWarnings("unchecked")
	public Playback(/*@ non_null @*/ T ... values)
	{
		this(0, values);
	}

	/**
	 * Creates a new Playback picker
	 * @param start_index The position of the first value to return
	 * @param values The values to play back
	 */
	public Playback(int start_index, /*@ non_null @*/ List<T> values)
	{
		m_values = values;
		m_index = start_index;
		m_startIndex = start_index;
	}

	/**
	 * Creates a new Playback picker
	 * @param values The values to play back
	 */
	public Playback(/*@ non_null @*/ List<T> values)
	{
		this(0, values);
	}

	/**
	 * Picks the next value in the list of the Playback picker. Typically, this method is expected to return non-null
	 * objects; a <tt>null</tt> return value is used to signal that no more
	 * objects will be produced. That is, once this method returns
	 * <tt>null</tt>, it should normally return <tt>null</tt> on all subsequent
	 * calls.
	 * @return The next value
	 */
	@Override
	public T pick()
	{
		if (m_index >= m_values.size() && !m_loop)
		{
			throw new NoMoreElementException();
		}
		T f = m_values.get(m_index);
		m_index++;
		if (m_index == m_values.size() && m_loop)
		{
			m_index = 0;
		}
		return f;
	}


	/**
	 * Puts the Playback picker back into its initial state. This means that the
	 * sequence of calls to {@link #pick()} will produce the same values
	 * as when the object was instantiated.
	 */
	@Override
	public void reset()
	{
		m_index = m_startIndex;
	}


	/**
	 * Picks a random string. Typically, this method is expected to return non-null
	 * objects; a <tt>null</tt> return value is used to signal that no more
	 * objects will be produced. That is, once this method returns
	 * <tt>null</tt>, it should normally return <tt>null</tt> on all subsequent
	 * calls.
	 * @return The random string.
	 */
	@Override
	public Playback<T> duplicate(boolean with_state)
	{
		Playback<T> lp = new Playback<>(m_startIndex, m_values);
		if (with_state)
		{
			lp.m_index = m_index;
		}
		if (!this.m_loop){
			lp.setLoop(false);
		}
		return lp;
	}


	/**
	 * Set the m_loop attribute of the playback picker. If the attribute is set to false, the next call to
	 * the {@link #pick()} method will return a {@link NoMoreElementException} after
	 * producing the last element of the list.
	 * @param b boolean value to enable playback loop or not
	 * @return The actual instance of the class.
	 */
	public Playback<T> setLoop(boolean b)
	{
		m_loop = b;
		return this;
	}

	/**
	 * Signals if the playback picker picked all the objects from m_values.
	 * Loop attributes must be false. If loop is true, the method will always return false.
	 * @return true if the picker picked all the objects from m_values and false if it's not the case.
	 */
	@Override
	public boolean isDone()
	{
		return (m_index >= (m_values.size())) && !m_loop;
	}

	@Override
	public Shrinkable<T> shrink(T o, Picker<Float> decision, float m)
	{
		return new PickSmallerComparable<T>(this, o);
	}

	@Override
	public PartNode getExplanation(Part p)
	{
		return getExplanation(p, NodeFactory.getFactory());
	}

	@Override
	public PartNode getExplanation(Part p, NodeFactory f)
	{
		PartNode root = f.getPartNode(p, this);
		int index = -1;
		Part head = p.head();
		if (head != null && head instanceof NthSuccessiveOutput)
		{
			index = ((NthSuccessiveOutput) head).getIndex();
		}
		if (index < 0 || (!m_loop && index > m_values.size()))
		{
			// Not a valid part, end there
			return root;
		}
		int actual_index = index % m_values.size();
		Part new_p = ComposedPart.compose(new NthElement(actual_index), new NthSuccessiveOutput(index));
		root.addChild(f.getPartNode(new_p, m_values));
		return root;
	}

	@Override
	public String toString()
	{
		StringBuilder out = new StringBuilder();
		out.append("Playback [");
		for (int i = 0; i < m_values.size(); i++)
		{
			if (i > 0)
			{
				out.append(",");
			}
			out.append(m_values.get(i));
		}
		out.append("]");
		return out.toString();
	}

	@Override
	public Shrinkable<T> shrink(T o)
	{
		return shrink(o, RandomFloat.instance, 1);
	}

	@Override
	public SequenceShrinkable<T> shrink(Picker<Float> d, float m)
	{
		if (m_shrinkable != null)
		{
			// The source of the values is shrinkable, so ask it
			Shrinkable<List<T>> shrunk = m_shrinkable.shrink(m_values);
			return new Playback<T>(shrunk, 0, shrunk.pick()).setLoop(false);
		}
		List<Integer> indices = new ArrayList<Integer>();
		int num_to_pick = (int) (m * (float) m_values.size());
		while (indices.size() < num_to_pick)
		{
			int index = (int) Math.floor(d.pick() * m_values.size());
			if (!indices.contains(index))
			{
				indices.add(index);
			}
		}
		Collections.sort(indices);
		List<T> values = new ArrayList<T>(indices.size());
		for (int index : indices)
		{
			values.add(m_values.get(index));
		}
		return new Playback<T>(values).setLoop(false);
	} 
	
	/**
	 * Gets the complete sequence of values that this picker is programmed to
	 * produce. 
	 * @return The list of values
	 */
	public List<T> getProgrammedSequence()
	{
		return m_values;
	}
	
	@Override
	public List<T> getSequence()
	{
		return m_values.subList(m_startIndex, m_index);
	}
}
