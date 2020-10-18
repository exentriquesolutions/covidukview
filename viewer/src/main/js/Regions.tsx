import * as React from 'react';
import {FunctionComponent, useEffect, useState} from "react";
import loadRegions from "./loadRegions";

export type Region = {
    id: number,
    name: string,
    type: string
}

const Regions: FunctionComponent = () => {
    const [regions, setRegions] = useState<Region[]>([])
    if (regions.length == 0) {
        loadRegions(setRegions);
    }

    return (
        <select id="regions">
            {
                regions.map(region =>
                    <option key={region.id} value={region.id}>{region.name} ({region.type})</option>
                )
            }
        </select>
    );
}

export default Regions
