import React from "react";

const SearchBox = ({searchChange}) => {
    return (
        <div className='pa2'>
            <input
                className='ba b--green bg-lightest-blue'
                type='search'
                placeholder='search items'
                onChange={searchChange}
            />
        </div>
    );
}

export default SearchBox;