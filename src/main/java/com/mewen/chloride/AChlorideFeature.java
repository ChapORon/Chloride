package com.mewen.chloride;

public abstract class AChlorideFeature
{
    protected Configuration config;

    public void SetConfig(Configuration config) {this.config = config;}
    public abstract void OnRegistration();
}
