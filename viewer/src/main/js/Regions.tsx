import * as React from 'react';
import {ChangeEvent, FunctionComponent, useState} from 'react';
import loadRegions from "./loadRegions";

export type RegionsProps = {
    onRegionChange: (regionId: number) => void
}

export type Region = {
    id: number,
    name: string,
    type: string
}

const Regions: FunctionComponent = (props: RegionsProps) => {
    const {onRegionChange} = props
    const [regions, setRegions] = useState<Region[]>([])
    if (regions.length == 0) {
        loadRegions(setRegions);
    }
    const parseOnChange = (changeEvent: ChangeEvent<HTMLSelectElement>) => {
        onRegionChange(parseInt(changeEvent.target.value))
    }

    return (
        <select id="regions" onChange={parseOnChange}>
            {
                regions.map(region =>
                    <option key={region.id} value={region.id}>{region.name} ({region.type})</option>
                )
            }
        </select>
    );
}

export default Regions
