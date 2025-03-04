/*
    Synthia, a data structure generator
    Copyright (C) 2019-2020 Laboratoire d'informatique formelle
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
 * Dummy main file. This file, when run, only displays a message
 * on the console.
 * @ingroup API
 */
public class Main
{
	private Main()
	{
		super();
		throw new UnsupportedOperationException("This class cannot be instantiated");
	}
	
	public static void main(String[] args)
	{
		System.out.println("Synthia - A data structure generator");
		System.out.println("(C) 2019-2021 Laboratoire d'informatique formelle");
		System.out.println("Université du Québec à Chicoutimi, Canada");
	}
}
