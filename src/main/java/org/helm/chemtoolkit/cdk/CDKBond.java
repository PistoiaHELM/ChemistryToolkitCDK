/*******************************************************************************
 * Copyright C 2015, The Pistoia Alliance
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package org.helm.chemtoolkit.cdk;

import java.util.ArrayList;
import java.util.List;

import org.helm.chemtoolkit.IAtomBase;
import org.helm.chemtoolkit.IBondBase;
import org.helm.chemtoolkit.IStereoElementBase;
import org.openscience.cdk.interfaces.IBond;

/**
 * @author chistyakov
 *
 */
public class CDKBond implements IBondBase {

  protected IBond bond;

  protected CDKStereoElement stereoElement;

  protected List<IBond> bonds;

  public IBond getAtomBond() {
    return bond;
  }

  /**
   * @param bond
   */
  public CDKBond(IBond bond) {
    this.bond = bond;
    bonds = new ArrayList<>();
    bonds.add(bond);
  }

  public CDKBond(IBond bond, IStereoElementBase stereo) {
    new CDKBond(bond);
    if (stereo != null && stereo instanceof CDKStereoElement)
      this.stereoElement = (CDKStereoElement) stereo;

  }

  /**
   *
   * {@inheritDoc}
   */
  @Override
  public IAtomBase getIAtom1() {

    return new CDKAtom(bond.getAtom(0), 0, bonds);
  }

  /**
   *
   * {@inheritDoc}
   */
  @Override
  public IAtomBase getIAtom2() {

    return new CDKAtom(bond.getAtom(1), 0, bonds);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IStereoElementBase getStereoElement() {

    return stereoElement;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getType() {
    return bond.getOrder().numeric();
  }

}
