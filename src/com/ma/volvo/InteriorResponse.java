package com.ma.volvo;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "Features_res")
public class InteriorResponse {

    @XmlElement(name = "StartWeek")
    private int startWeek;

    @XmlElement(name = "EndWeek")
    private int endWeek;

    @XmlElement(name = "Pno12")
    private String pno12;

    @XmlElementWrapper(name = "FeatureList")
    @XmlElement(name = "Feature")
    private List<Feature> featureList;

    @XmlElementWrapper(name = "OptionList")
    @XmlElement(name = "Option")
    private List<String> optionList;

    @XmlElementWrapper(name = "CUList")
    @XmlElement(name = "CU")
    private List<InteriorRoom> cuList;
    
    @XmlTransient
    private String color;
    
    @XmlTransient
    private String upholstrey;
/*    
    @XmlTransient
    private List<ColorUpholstery> colorUpholsteryList;
    
    @XmlTransient
    private List<String> roomColorList;
    @XmlTransient
    private List<String> roomUpholsteryList; 
*/
    public InteriorResponse() {
    }

    public InteriorResponse(int startWeek, int endWeek, String pno12) {
        super();
        this.startWeek = startWeek;
        this.endWeek = endWeek;
        this.pno12 = pno12;
    }

    @XmlTransient
    public int getStartWeek() {
        return startWeek;
    }

    public void setStartWeek(int startWeek) {
        this.startWeek = startWeek;
    }

    @XmlTransient
    public int getEndWeek() {
        return endWeek;
    }

    public void setEndWeek(int endWeek) {
        this.endWeek = endWeek;
    }

    @XmlTransient
    public String getPno12() {
        return pno12;
    }

    public void setPno12(String pno12) {
        this.pno12 = pno12;
    }

    @XmlTransient
    public List<Feature> getFeatureList() {
        return featureList;
    }

    public void setFeatureList(List<Feature> featureList) {
        this.featureList = featureList;
    }

    @XmlTransient
    public List<String> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<String> optionList) {
        this.optionList = optionList;
    }

    @XmlTransient
    public List<InteriorRoom> getCuList() {
        return cuList;
    }

    public void setCuList(List<InteriorRoom> cuList) {
        this.cuList = cuList;
    }
    
    @XmlTransient
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	@XmlTransient
	public String getUpholstrey() {
		return upholstrey;
	}

	public void setUpholstrey(String upholstrey) {
		this.upholstrey = upholstrey;
	}
	
	public void setColorUpholstrey() {
        if(!cuList.isEmpty()) {
        	for (InteriorRoom colUph : cuList) {
        		setColor(colUph.getColor()); 
        		setUpholstrey(colUph.getUpholstery());
        	}
        }
	}
	
	/*
	public List<String> getRoomColorList() {
		return roomColorList;
	}

	public void setRoomColorList(List<String> roomColorList) {
		this.roomColorList = roomColorList;
	}

	public List<String> getRoomUpholsteryList() {
		return roomUpholsteryList;
	}

	public void setRoomUpholsteryList(List<String> roomUpholsteryList) {
		this.roomUpholsteryList = roomUpholsteryList;
	}

	public void addRoomColor(String rooomColor) {
			if(this.roomColorList == null) {
				this.roomColorList = new ArrayList<String>();
			}
			roomColorList.add(rooomColor);
	}
	
	public void addUpholstery(String rooomUpholstery) {
		if(this.roomUpholsteryList == null) {
			this.roomUpholsteryList = new ArrayList<String>();
		}
		roomUpholsteryList.add(rooomUpholstery);
	}
	
	public List<ColorUpholstery> getColorUpholsteryList() {
		return colorUpholsteryList;
	}

	public void setColorUpholsteryList(List<ColorUpholstery> colorUpholsteryList) {
		this.colorUpholsteryList = colorUpholsteryList;
	}

	public void addColorUpholstery(String roomColor, String rooomUpholstery) {
		if(this.colorUpholsteryList == null) {
			this.colorUpholsteryList = new ArrayList<ColorUpholstery>();
		}
		ColorUpholstery colorUpholstery = new ColorUpholstery(roomColor, rooomUpholstery);
		this.colorUpholsteryList.add(colorUpholstery);
	}
*/

	@Override
    public String toString() {
        return "InteriorResponse{" + "sWeek=" + startWeek + ", eWeek=" + endWeek + ", pno12='" + pno12 + '\'' + ", featureList=" + featureList + ", optionList="
            + optionList + '}';
    }
}