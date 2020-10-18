import fetch from 'cross-fetch';
import {Region} from "./Regions";

const loadRegions = (handler: (regions: Region[]) => void) => {
    fetch('/regions')
        .then(async res => {
            if (res.status == 200) {
                handler(await res.json())
            }
        })
}

export default loadRegions
