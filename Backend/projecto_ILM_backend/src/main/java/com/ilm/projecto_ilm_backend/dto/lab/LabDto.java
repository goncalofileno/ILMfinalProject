package com.ilm.projecto_ilm_backend.dto.lab;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * LabDto is a Data Transfer Object (DTO) class.
 * It is used to transfer data between processes.
 * In this case, it is used to send Lab data in the response.
 */
@XmlRootElement
public class LabDto {

    private String local;
    private String contact;

    /**
     * Default constructor.
     */
    public LabDto() {
    }

    /**
     * Overloaded constructor.
     *
     * @param local   the local of the lab
     * @param contact the contact of the lab
     */
    public LabDto(String local, String contact) {
        this.local = local;
        this.contact = contact;
    }

    /**
     * Returns the local of the lab.
     *
     * @return the local of the lab
     */
    @XmlElement
    public String getLocal() {
        return local;
    }

    /**
     * Sets the local of the lab.
     *
     * @param local the new local of the lab
     */
    public void setLocal(String local) {
        this.local = local;
    }

    /**
     * Returns the contact of the lab.
     *
     * @return the contact of the lab
     */
    @XmlElement
    public String getContact() {
        return contact;
    }

    /**
     * Sets the contact of the lab.
     *
     * @param contact the new contact of the lab
     */
    public void setContact(String contact) {
        this.contact = contact;
    }
}