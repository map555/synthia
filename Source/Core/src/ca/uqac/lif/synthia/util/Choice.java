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
package ca.uqac.lif.synthia.util;

import java.util.ArrayList;
import java.util.List;

import ca.uqac.lif.synthia.Picker;
import ca.uqac.lif.synthia.Shrinkable;
import ca.uqac.lif.synthia.random.RandomFloat;
import ca.uqac.lif.synthia.relative.PickSmallerComparable;

/**
 * Picks an element from a collection, where the probability of picking
 * each element can be user-defined. Elements are added to the collection
 * by calling {@link #add(Object, Number) add()}. For example, the following
 * code snippet creates a collection of three strings, where <tt>"A"</tt>
 * has twice the odds of being picked compared to <tt>"B"</tt> and <tt>"C"</tt>:
 * <pre>
 * RandomFloat r = new RandomFloat();
 * Choice&lt;String&gt; ep = new Choice&lt;String&gt;(r);
 * ep.add("A", 0.5).add("B", 0.25).add("C", 0.25);</pre>
 * When adding elements, one must make sure that the sum of probabilities
 * is equal to 1.
 * @param <T> The type of the object to pick
 * @ingroup API
 */
public class Choice<T> implements Shrinkable<T>
{
	/**
	 * A list storing each element with its associated probability
	 */
	/*@ non_null @*/ protected List<ProbabilityChoice<T>> m_choices;
	
	/**
	 * A picker used to choose the element
	 */
	/*@ non_null @*/ protected Picker<Float> m_floatPicker;
	
	/**
	 * Creates a new element picker
	 * @param picker A picker used to choose the element
	 */
	public Choice(/*@ non_null @*/ Picker<Float> picker)
	{
		super();
		m_choices = new ArrayList<ProbabilityChoice<T>>();
		m_floatPicker = picker;
	}
	
	/**
	 * Adds an object-probability association
	 * @param pc The association
	 * @return This element picker
	 */
	/*@ non_null @*/ public Choice<T> add(/*@ non_null @*/ ProbabilityChoice<T> pc)
	{
		m_choices.add(pc);
		return this;
	}
	
	/**
	 * Adds an object-probability association
	 * @param t The object
	 * @param p The probability, must be between 0 and 1
	 * @return This element picker
	 */
	/*@ non_null @*/ public Choice<T> add(/*@ non_null @*/ T t, /*@ non_null @*/ Number p)
	{
		ProbabilityChoice<T> pc = new ProbabilityChoice<T>(new Constant<T>(t), p);
		return add(pc);
	}
	
	/**
	 * Adds an object-probability association
	 * @param t The picker for an object
	 * @param p The probability, must be between 0 and 1
	 * @return This element picker
	 */
	/*@ non_null @*/ public Choice<T> add(/*@ non_null @*/ Picker<T> t, /*@ non_null @*/ Number p)
	{
		ProbabilityChoice<T> pc = new ProbabilityChoice<T>(t, p);
		return add(pc);
	}


	/**
	 * Puts the element picker back into its initial state. This means that the
	 * sequence of calls to {@link #pick()} will produce the same values
	 * as when the object was instantiated.
	 */
	@Override
	public void reset()
	{
		m_floatPicker.reset();
		for (ProbabilityChoice<?> pc : m_choices)
		{
			pc.reset();
		}
	}


	/**
	 * Picks a random element. Typically, this method is expected to return non-null
	 * objects; a <tt>null</tt> return value is used to signal that no more
	 * objects will be produced. That is, once this method returns
	 * <tt>null</tt>, it should normally return <tt>null</tt> on all subsequent
	 * calls.
	 * @return The random element.
	 */
	@Override
	public T pick() 
	{
		float[] probs = new float[m_choices.size()];
		float total_prob = 0f;
		for (int i = 0; i < probs.length; i++)
		{
			total_prob += m_choices.get(i).getProbability();
			probs[i] = total_prob;
		}
		float f = m_floatPicker.pick();
		int index = 0;
		while (index < probs.length)
		{
			if (f <= probs[index])
			{
				break;
			}
			index++;
		}
		if (index >= m_choices.size())
		{
			return null;
		}
		return m_choices.get(index).getObject();
	}

	/**
	 * Creates a copy of the element picker.
	 * @param with_state If set to <tt>false</tt>, the returned copy is set to
	 * the class' initial state (i.e. same thing as calling the picker's
	 * constructor). If set to <tt>true</tt>, the returned copy is put into the
	 * same internal state as the object it is copied from.
	 * @return The copy of the element picker
	 */
	@Override
	/*@ pure non_null @*/ public Choice<T> duplicate(boolean with_state)
	{
		Choice<T> ep = new Choice<T>(m_floatPicker.duplicate(with_state));
		for (ProbabilityChoice<T> pc : m_choices)
		{
			ep.m_choices.add(pc.duplicate(with_state));
		}
		return ep;
	}
	
	/**
	 * Simple data structure asssociating an object with
	 * a probability.
	 * @param <T> The type of the object to which a probability is associated
	 */
	public static class ProbabilityChoice<T>
	{
		/**
		 * The probability of taking this object
		 */
		protected float m_probability;
		
		/**
		 * The object corresponding to that object
		 */
		/*@ non_null @*/ protected Picker<T> m_object;
		
		/**
		 * Creates a new probability-object association
		 * @param t The object
		 * @param p The probability of picking this node
		 */
		public ProbabilityChoice(/*@ non_null @*/ Picker<T> t, /*@ non_null @*/ Number p)
		{
			super();
			m_object = t;
			m_probability = p.floatValue();
		}
		
		/**
		 * Resets the underlying picker for this probability choice.
		 */
		public void reset()
		{
			m_object.reset();
		}
		
		/**
		 * Gets the probability for this association
		 * @return The probability
		 */
		/*@ pure @*/ public float getProbability()
		{
			return m_probability;
		}
		
		/**
		 * Gets the tree node for this association
		 * @return The node
		 */
		/*@ pure non_null @*/ public T getObject()
		{
			return m_object.pick();
		}


		/**
		 * Returns the last picked element and his probability to get picked into a string.
		 * @return The string containing the string representation of the object and his pick probability.
		 */
		@Override
		public String toString()
		{
			return m_object.toString() + " (" + m_probability + ")";
		}
		
		/**
		 * Duplicates this probability-node association
		 * @param with_state If set to <tt>true</tt>, the node is duplicated
		 * with its state
		 * @return A duplicate of this association
		 */
		/*@ pure non_null @*/ public ProbabilityChoice<T> duplicate(boolean with_state)
		{
			return new ProbabilityChoice<T>(m_object, m_probability);
		}
	}
	
	/**
	 * Gets the total number of alternatives available in this picker.
	 * @return The number of choices
	 */
	public int getChoiceCount()
	{
		return m_choices.size();
	}
	
	@Override
	public Shrinkable<T> shrink(T o, Picker<Float> decision, float m)
	{
		return new PickSmallerComparable<T>(this, o);
	}

	@Override
	public Shrinkable<T> shrink(T o)
	{
		return shrink(o, RandomFloat.instance, 1);
	}
}
