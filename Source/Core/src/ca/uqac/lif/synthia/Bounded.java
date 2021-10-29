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
package ca.uqac.lif.synthia;

/**
 * Interface who extends Picker. This interface will be used to signal
 * that the picker enumerates all values from a set.
 * 
 * @ingroup API
 */
public interface Bounded<T> extends Picker<T>
{
	/**
	 * Signals if the picker enumerates all values from a set.
	 * @return true if the picker enumerates all the values and false if it's not the case.
	 */
 public boolean isDone();
}
