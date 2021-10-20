package ca.uqac.lif.synthia.enumerative;

import ca.uqac.lif.synthia.Bounded;
import ca.uqac.lif.synthia.exception.NoMoreElementException;

/**
 * Picker who implements EnumerativePicker. This picker enumerates the boolean value
 * (<Boolean>false</Boolean>, <Boolean>true</Boolean>) and throws a
 * {@link NoMoreElementException} if the picker picks another value after
 * the picker has finished to enumerates the boolean values.
 */
public class AllBooleans implements Bounded<Boolean>
{

	/**
	 * Internal picker used to generate the objects generated by the AllBooleans picker
	 */
	protected AllIntegers m_picker;



	/**
	 * Private constructor used for the duplication of the picker.
	 * @param picker The internal picker used by the Allbooleans picker to generate objects.
	 */
	private AllBooleans(AllIntegers picker)
	{
		m_picker = picker;

	}

	/**
	 * Default constructor using the <tt>false</tt> default value of the {@link AllIntegers} scramble
	 * flag.
	 */
	public AllBooleans()
	{
		m_picker = new AllIntegers(0, 1);
	}

	/**
	 * Constructor taking a parameter for the scramble flag.
	 * @param scramble <tt>true</tt> to generate scramble values or <tt>false</tt> to generate
	 *                 values in order.
	 */
	public AllBooleans(boolean scramble)
	{
		m_picker = new AllIntegers(0, 1, scramble);
	}

	@Override
	public void reset()
	{
		m_picker.reset();
	}

	@Override
	public Boolean pick()
	{

		return m_picker.pick() == 1;
	}

	@Override
	public AllBooleans duplicate(boolean with_state)
	{
		return new AllBooleans( m_picker.duplicate(with_state));
	}

	/**
	 * Signals if the AllBooleans picker picked all the objects from m_picker.
	 * Loop attributes must be false. If loop is true, the method will always return false.
	 * @return true if the picker picked all the objects from m_picker and false if it's not the case.
	 */
	@Override
	public boolean isDone()
	{
		return m_picker.isDone();
	}
}
