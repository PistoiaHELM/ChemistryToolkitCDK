/*******************************************************************************
 * Copyright C 2015, The Pistoia Alliance
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ******************************************************************************/
package org.helm.chemtoolkit.cdk;

import java.util.ArrayList;
import java.util.List;

import org.helm.chemtoolkit.AbstractMolecule.Flag;
import org.helm.chemtoolkit.CTKException;
import org.helm.chemtoolkit.IAtomBase;
import org.helm.chemtoolkit.IBondBase;
import org.openscience.cdk.Atom;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IPseudoAtom;

/**
 * @author chistyakov
 *
 */
public class CDKAtom extends IAtomBase {

  protected IAtom atom;

  protected int rGroup;

  protected List<CDKBond> bonds;

  protected int boundCount;

  @Override
  public IAtom getMolAtom() {
    return atom;
  }

  /**
   * @param atom
   */
  public CDKAtom(IAtom atom) {
    new CDKAtom(atom, 0, new ArrayList<IBond>());

  }

  public CDKAtom(IAtom atom, int rGroup) {
    new CDKAtom(atom, rGroup, new ArrayList<IBond>());

  }

  /**
   * @param atom2
   * @param i
   * @param cdkBonds
   */
  public CDKAtom(IAtom atom, int rGroup, List<IBond> bonds) {
    this.atom = atom;
    this.flag = Flag.NONE;
    this.rGroup = rGroup;
    this.bonds = new ArrayList<CDKBond>();
    for (IBond bond : bonds) {
      this.bonds.add(new CDKBond(bond));
    }

  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public int getIBondCount() {
    return bonds.size();
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public IBondBase getIBond(int arg0) throws CTKException {
    CDKBond bond = null;
    try

    {
      bond = bonds.get(arg0);
    } catch (IndexOutOfBoundsException e) {
      throw new CTKException("bond doesn't exist", e);
    }
    return bond;
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public int getRgroup() {

    return rGroup;
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public int getIAtno() {
    // TODO Auto-generated method stub
    return atom.getAtomicNumber();

  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public boolean compare(Object obj) {
    if (!(obj instanceof CDKAtom)) {
      return false;
    }

    IAtom toCompare = ((CDKAtom) obj).getMolAtom();

    if ((toCompare instanceof IPseudoAtom) && (atom instanceof IPseudoAtom)) {

      return ((IPseudoAtom) atom).getLabel().equals(((IPseudoAtom) toCompare).getLabel());
    } else if ((toCompare instanceof Atom) && (atom instanceof Atom)) {
      return ((Atom) atom).compare(obj);

    }

    return false;
  }

  /**
   * {@inheritDoc}
   * 
   * @throws CTKException
   */
  @Override
  public void setRgroup(int rGroup) throws CTKException {
    if (atom instanceof IPseudoAtom) {
      ((IPseudoAtom) atom).setLabel(((IPseudoAtom) atom).getLabel().replace(String.valueOf(this.rGroup), String.valueOf(rGroup)));
      this.rGroup = rGroup;
      this.flag = Flag.PROCESSED;
    } else
      throw new CTKException("unable to set group id, the atom is not a PseudoAtom");

  }

}
