package ca.uqac.lif.synthia.random;

import ca.uqac.lif.synthia.Picker;
import ca.uqac.lif.synthia.Shrinkable;
import ca.uqac.lif.synthia.string.RandomSubString;
import ca.uqac.lif.synthia.util.NothingPicker;

import java.util.ArrayList;
import java.util.List;

/**
 * An equivalent of {@link RandomSubString} but for lists.
 *
 * @param <T> The type of the list used by the {@link Picker}.
 * @ingroup API
 */
public class RandomSubList<T> implements Shrinkable<List<T>>
{

	/**
	 * {@link RandomBoolean} used to determine if a element of the list will be added in the sublist
	 * or not.
	 */
	protected RandomBoolean m_pick;

	/**
	 * If the element will be added in the sublist, this {@link RandomBoolean} is used to determine if
	 * the element will be added as is or be first passed to the {@link Shrinkable}
	 * {@link #m_listReducer}.
	 */
	protected RandomBoolean m_asIs;

	/**
	 * {@link Shrinkable} to reduce elements when {@link #m_asIs} decides that the curent element
	 * will be reduced. If it's the case, this picker uses the  {@link #shrink(List)} method,
	 * passes the element as parameter and uses the {@link Picker#pick()} method of the created
	 * picker to generated the element to add in the sublist.
	 */
	protected Shrinkable<T> m_listReducer;

	/**
	 * The list of elements used to generate sublists.
	 */
	protected List<T> m_elements;

	/**
	 * Private constructor used in the {@link #duplicate(boolean)} method.
	 *
	 * @param pick         The {@link #m_pick} attribute.
	 * @param as_is        The {@link #m_asIs} attribute.
	 * @param elements     The {@link #m_elements} attribute.
	 * @param list_reducer The {@link #m_listReducer} attribute.
	 */
	private RandomSubList(RandomBoolean pick, RandomBoolean as_is, List<T> elements,
			Shrinkable<T> list_reducer)
	{
		m_pick = pick;
		m_asIs = as_is;
		m_elements = elements;
		m_listReducer = list_reducer;
	}

	/**
	 * Public method to create a new instance of the class.
	 *
	 * @param elements     The list of elements used to generate sublists.
	 * @param list_reducer The {@link Shrinkable} used to reduce elements of the original list.
	 */
	public RandomSubList(List<T> elements, Shrinkable<T> list_reducer)
	{
		m_pick = new RandomBoolean();
		m_asIs = new RandomBoolean();
		m_elements = elements;
		m_listReducer = list_reducer;
	}

	@Override
	public void reset()
	{
		m_pick.reset();
		m_asIs.reset();
		m_listReducer.reset();
	}

	@Override
	public List<T> pick()
	{
		List<T> output_list = new ArrayList<>();
		for (T m_element : m_elements)
		{
			if (m_pick.pick())
			{
				if (m_asIs.pick())
				{
					output_list.add(m_element);
				}
				else
				{
					output_list.add(m_listReducer.shrink(m_element, new RandomFloat(), 1).pick());
				}
			}
		}

		return output_list;
	}

	@Override
	public Picker<List<T>> duplicate(boolean with_state)
	{
		return new RandomSubList<T>(m_pick.duplicate(with_state), m_asIs.duplicate(with_state),
				new ArrayList<T>(m_elements), (Shrinkable<T>) m_listReducer.duplicate(with_state));

	}

	/**
	 * Create a new {@link RandomSubList} picker based on a given list. If this list is
	 * empty, the method will return a {@link NothingPicker}. The new instance will also have the same
	 * internal states for the {@link #m_listReducer}, {@link #m_pick} and {@link #m_asIs} attributes
	 * as the original one.
	 *
	 * @param element The list used by the new {@link RandomSubList} picker to produce sublists.
	 * @return The new instance of the class or a {@link NothingPicker}.
	 * @warning Depending of the content of the list, it is possible that the returned object is a
	 * {@link NothingPicker}. If a {@link RandomSubList} is returned, it is also possible that some of
	 * the sublists generated by the {@link #pick()} method of this instance could generate
	 * {@link ca.uqac.lif.synthia.NoMoreElementException}'s.
	 */
	@Override 
	public Shrinkable<List<T>> shrink(List<T> element, Picker<Float> decision, float magnitude)
	{
		if (!element.getClass().getSimpleName().equals("ArrayList"))
		{
			return new NothingPicker<List<T>>();
		}
		else
		{
			List<T> objects = (List<T>) element;

			if (objects.isEmpty())
			{
				return new NothingPicker<List<T>>();
			}

			return new RandomSubList<T>(m_pick, m_asIs.duplicate(true), objects,
					(Shrinkable<T>) m_listReducer.duplicate(true));
		}
	}

	@Override
	public Shrinkable<List<T>> shrink(List<T> o)
	{
		return shrink(o, RandomFloat.instance, 1);
	}
}
