package com.vehicle.detection.model;

import java.util.List;
/**
 * Created by Ayushi on 23/01/19.
 */
public class Wheel {

	List<Position> positionList;
	List<Material> materialList;

	public List<Position> getPositionList() {
		return positionList;
	}

	public void setPositionList(List<Position> positionList) {
		this.positionList = positionList;
	}

	public List<Material> getMaterialList() {
		return materialList;
	}

	public void setMaterialList(List<Material> materialList) {
		this.materialList = materialList;
	}
}
