function MultiSelectTag(el, customs = { shadow: false, rounded: true }) {
    // Initialize variables
    var element = null,
        options = null,
        customSelectContainer = null,
        wrapper = null,
        btnContainer = null,
        body = null,
        inputContainer = null,
        inputBody = null,
        input = null,
        button = null,
        drawer = null,
        ul = null;

    // Customize tag colors
    var tagColor = customs.tagColor || {};
    tagColor.textColor = "#0372B2";
    tagColor.borderColor = "#0372B2";
    tagColor.bgColor = "#C0E6FC";

    // Initialize DOM Parser
    var domParser = new DOMParser();

    // Initialize
    init();

    function init() {
        // DOM element initialization
        element = document.getElementById(el);
        createElements();
        initOptions();  // Populate options
        enableItemSelection();
        setValues(false);

        // Event listeners
        button.addEventListener('click', () => {
            if (drawer.classList.contains('hidden')) {
                initOptions();  // Populate options
                enableItemSelection();
                drawer.classList.remove('hidden');
                input.focus();
            } else {
                drawer.classList.add('hidden');
            }
        });

        input.addEventListener('keyup', (e) => {
            initOptions(e.target.value);  // Update options based on search
            enableItemSelection();
        });

        input.addEventListener('keydown', (e) => {
            if (e.key === 'Backspace' && !e.target.value && inputContainer.childElementCount > 1) {
                const child = body.children[inputContainer.childElementCount - 2].firstChild;
                const option = options.find((op) => op.value == child.dataset.value);
                option.selected = false;
                removeTag(child.dataset.value);
                setValues();
            }
        });

        window.addEventListener('click', (e) => {
            if (!customSelectContainer.contains(e.target)) {
                if ((e.target.nodeName !== "LI") && (e.target.getAttribute("class") !== "input_checkbox")) {
                    // Hide the list option only if we click outside of it
                    drawer.classList.add('hidden');
                } else {
                    // Enable again the click on the list options
                    enableItemSelection();
                }
            }
        });
    }

    function createElements() {
        // Create custom elements
        options = getOptions();
        element.classList.add('hidden');

        // .multi-select-tag
        customSelectContainer = document.createElement('div');
        customSelectContainer.classList.add('mult-select-tag');

        // .container
        wrapper = document.createElement('div');
        wrapper.classList.add('wrapper');

        // body
        body = document.createElement('div');
        body.classList.add('body');
        if (customs.shadow) {
            body.classList.add('shadow');
        }
        if (customs.rounded) {
            body.classList.add('rounded');
        }

        // .input-container
        inputContainer = document.createElement('div');
        inputContainer.classList.add('input-container');

        // input
        input = document.createElement('input');
        input.classList.add('input');
        input.placeholder = `${customs.placeholder || 'Search...'}`;

        inputBody = document.createElement('inputBody');
        inputBody.classList.add('input-body');
        inputBody.append(input);

        body.append(inputContainer);

        // .btn-container
        btnContainer = document.createElement('div');
        btnContainer.classList.add('btn-container');

        // button
        button = document.createElement('button');
        button.type = 'button';
        btnContainer.append(button);

        const icon = domParser.parseFromString(
            `<svg xmlns="http://www.w3.org/2000/svg" width="100%" height="100%" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <polyline points="18 15 12 21 6 15"></polyline>
            </svg>`, 'image/svg+xml').documentElement;

        button.append(icon);

        body.append(btnContainer);
        wrapper.append(body);

        drawer = document.createElement('div');
        drawer.classList.add(...['drawer', 'hidden']);
        if (customs.shadow) {
            drawer.classList.add('shadow');
        }
        if (customs.rounded) {
            drawer.classList.add('rounded');
        }
        drawer.append(inputBody);
        ul = document.createElement('ul');

        drawer.appendChild(ul);

        customSelectContainer.appendChild(wrapper);
        customSelectContainer.appendChild(drawer);

        // Place TailwindTagSelection after the element
        if (element.nextSibling) {
            element.parentNode.insertBefore(customSelectContainer, element.nextSibling);
        } else {
            element.parentNode.appendChild(customSelectContainer);
        }
    }

    function createElementInSelectList(option, selected = false) {
        // Tạo một phần tử <li> trong danh sách dropdown
        const li = document.createElement('li');
        li.innerHTML = option.label;
        li.dataset.value = option.value;
        li.dataset.idClient = option.value; // Thêm thuộc tính data-id

        // Thay đổi màu nền nếu đã chọn
        if (selected) {
            li.style.backgroundColor = tagColor.bgColor;
        }

        // Thêm sự kiện click vào thẻ <li>
        li.addEventListener('click', (e) => {
            const idClient = e.target.dataset.idClient;
            if (idClient) {
                // Thực hiện yêu cầu GET đến endpoint
                fetch(`/bill/addClientInBill/${idClient}`, {
                    method: 'GET'
                })
                    .then(response => {
                        if (response.ok) {
                            return response.text(); // Nhận phản hồi dạng text
                        } else {
                            throw new Error('Yêu cầu không thành công');
                        }
                    })
                    .then(data => {
                        console.log(data); // Xóa khách hàng thành công!
                        window.location.href = `/bill/bill-detail/${document.getElementById('idBill').value}`; // Chuyển hướng trang nếu cần
                    })
                    .catch(error => console.error('Lỗi:', error));
            }
        });

        ul.appendChild(li);
    }


    function initOptions(val = null) {
        ul.innerHTML = '';
        // Get value from hidden input
        const hiddenInputValue = document.getElementById('idClient').value;

        // Filter and display only the options that match the search query
        let filteredOptions = options.filter(option =>
            val ? option.label.toLowerCase().includes(val.toLowerCase()) : true
        );

        // Display only 5 options if there are more
        let displayedCount = 0;
        for (var option of filteredOptions) {
                // Check if the option should be selected by default
                const isSelected = hiddenInputValue.split(',').includes(option.value);
                createElementInSelectList(option, isSelected);
                if (isSelected) {
                    option.selected = true;
                    createTag(option); // Create tag for the selected option
                }
                displayedCount++;

        }
    }

    function createTag(option) {
        // Create and show selected item as tag
        inputContainer.innerHTML = ''; // Clear previous tags
        const itemDiv = document.createElement('div');
        itemDiv.classList.add('item-container');
        itemDiv.style.color = tagColor.textColor || '#2c7a7b';
        itemDiv.style.borderColor = tagColor.borderColor || '#81e6d9';
        itemDiv.style.background = tagColor.bgColor || '#e6fffa';
        const itemLabel = document.createElement('div');
        itemLabel.classList.add('item-label');
        itemLabel.style.color = tagColor.textColor || '#2c7a7b';
        itemLabel.innerHTML = option.label;
        itemLabel.dataset.value = option.value;
        console.log('kh hang chon co id la ' + option.value)

        const itemClose = domParser.parseFromString(
            `<svg xmlns="http://www.w3.org/2000/svg" width="100%" height="100%" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="item-close-svg">
                <line x1="18" y1="6" x2="6" y2="18"></line>
                <line x1="6" y1="6" x2="18" y2="18"></line>
            </svg>`, 'image/svg+xml').documentElement;

        itemClose.addEventListener('click', (e) => {

            const unselectOption = options.find((op) => op.value == option.value);
            unselectOption.selected = false;
            inputContainer.innerHTML = ''; // Clear the selected tag
            fetch(`/bill/removeClientInBill/${itemLabel.dataset.value}`, {
                method: 'GET'
            })
                .then(response => {
                    if (response.ok) {
                        return response.text(); // Nhận phản hồi dạng text
                    } else {
                        throw new Error('Yêu cầu không thành công');
                    }
                })
                .then(data => {
                    console.log(data); // Xóa khách hàng thành công!
                    window.location.href = `/bill/bill-detail/${document.getElementById('idBill').value}`; // Chuyển hướng trang nếu cần
                })
                .catch(error => console.error('Lỗi:', error));

            initOptions();
            setValues();
        });

        itemDiv.appendChild(itemLabel);
        itemDiv.appendChild(itemClose);
        inputContainer.append(itemDiv);
    }

    function enableItemSelection() {
        // Add click listener to the list items
        for (var li of ul.children) {
            li.addEventListener('click', (e) => {
                options.forEach((op) => op.selected = false); // Deselect all options
                options.find((o) => o.value == e.target.dataset.value).selected = true; // Select the clicked option
                input.value = null;
                initOptions();
                setValues();
                createTag(options.find((o) => o.value == e.target.dataset.value)); // Create tag for the selected option
            });
        }
    }

    function setValues(fireEvent = true) {
        // Update element final values
        selected_values = [];
        for (var i = 0; i < options.length; i++) {
            element.options[i].selected = options[i].selected;
            if (options[i].selected) {
                selected_values.push(options[i].value);
            }
        }

        if (fireEvent) {
            let event = new Event('change', { bubbles: true });
            element.dispatchEvent(event);
        }
    }

    function getOptions() {
        // Get options from the original <select> element
        return [...element.options].map((op) => {
            return {
                value: op.value,
                label: op.label,
                selected: op.selected,
            };
        });
    }

    function removeTag(value) {
        // Remove tag for the given value
        const tagToRemove = inputContainer.querySelector(`.item-label[data-value="${value}"]`);
        if (tagToRemove) {
            tagToRemove.parentElement.remove();
        }
    }
}
