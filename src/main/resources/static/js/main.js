// MemeOverflow - Main JavaScript Functions

document.addEventListener('DOMContentLoaded', function () {
  initializePage();
});

function initializePage() {
  // Initialize correct tab on page load
  const currentType = document.getElementById('inputType')?.value || 'commit';
  if (typeof switchTab === 'function') {
    switchTab(currentType);
  }

  // Setup form submission handler
  setupFormHandler();

  // Setup auto-resize for textareas
  setupTextareaAutoResize();

  // Add example button handlers
  setupExampleButtons();
}

function setupFormHandler() {
  const form = document.getElementById('memeForm');
  if (form) {
    form.addEventListener('submit', function () {
      showLoading();
      disableGenerateButton();
    });
  }
}

function setupTextareaAutoResize() {
  document.querySelectorAll('textarea').forEach(textarea => {
    textarea.addEventListener('input', function () {
      this.style.height = 'auto';
      this.style.height = this.scrollHeight + 'px';
    });
  });
}

function setupExampleButtons() {
  document.querySelectorAll('.example-chip').forEach(chip => {
    chip.addEventListener('click', function () {
      const text = this.textContent;
      fillExample(text);
    });
  });
}

function switchTab(tabName) {
  // Hide all tab contents
  document.querySelectorAll('.tab-content').forEach(tab => {
    tab.style.display = 'none';
  });

  // Remove active class from all buttons
  document.querySelectorAll('.tab-button').forEach(btn => {
    btn.classList.remove('active');
  });

  // Show selected tab and mark button as active
  const selectedTab = document.getElementById(tabName + '-tab');
  if (selectedTab) {
    selectedTab.style.display = 'block';
  }

  // Find and activate the correct tab button
  const tabButtons = document.querySelectorAll('.tab-button');
  tabButtons.forEach(btn => {
    if (btn.textContent.toLowerCase().includes(tabName) ||
      btn.onclick?.toString().includes(tabName)) {
      btn.classList.add('active');
    }
  });

  // Update hidden input type
  const inputTypeField = document.getElementById('inputType');
  if (inputTypeField) {
    inputTypeField.value = tabName;
  }

  // Enable/disable the appropriate input
  document.querySelectorAll('textarea[name="input"]').forEach(textarea => {
    textarea.disabled = true;
    textarea.removeAttribute('required');
  });

  const activeInput = document.querySelector('#' + tabName + '-input');
  if (activeInput) {
    activeInput.disabled = false;
    activeInput.setAttribute('required', 'required');
  }
}

function fillExample(text) {
  const activeInput = document.querySelector('textarea[name="input"]:not([disabled])');
  if (activeInput) {
    activeInput.value = text;
    activeInput.focus();

    // Trigger auto-resize
    activeInput.style.height = 'auto';
    activeInput.style.height = activeInput.scrollHeight + 'px';
  }
}

function shareImage() {
  if (navigator.share) {
    navigator.share({
      title: 'Check out this programming meme!',
      text: 'Made with MemeOverflow - Turn your code commits into hilarious memes!',
      url: window.location.href
    }).catch(err => {
      console.error('Error sharing:', err);
      fallbackShare();
    });
  } else {
    fallbackShare();
  }
}

function fallbackShare() {
  if (navigator.clipboard) {
    navigator.clipboard.writeText(window.location.href).then(() => {
      showNotification('Link copied to clipboard!', 'success');
    }).catch(() => {
      showManualShare();
    });
  } else {
    showManualShare();
  }
}

function showManualShare() {
  const url = window.location.href;
  prompt('Copy this link to share:', url);
}

function regenerate() {
  const activeInput = document.querySelector('textarea[name="input"]:not([disabled])');
  if (activeInput && activeInput.value) {
    document.getElementById('memeForm')?.submit();
  } else {
    showNotification('Please enter some text first!', 'warning');
  }
}

function downloadImage() {
  const img = document.querySelector('.meme-container img');
  if (img) {
    const link = document.createElement('a');
    link.href = img.src;
    link.download = 'meme-overflow-meme.jpg';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    showNotification('Download started!', 'success');
  }
}

function showLoading() {
  const loading = document.getElementById('loading');
  if (loading) {
    loading.classList.add('show');
  }
}

function hideLoading() {
  const loading = document.getElementById('loading');
  if (loading) {
    loading.classList.remove('show');
  }
}

function disableGenerateButton() {
  const btn = document.getElementById('generateBtn');
  if (btn) {
    btn.disabled = true;
    btn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Generating...';
  }
}

function enableGenerateButton() {
  const btn = document.getElementById('generateBtn');
  if (btn) {
    btn.disabled = false;
    btn.innerHTML = '<i class="fas fa-magic"></i> Generate Meme';
  }
}

function showNotification(message, type = 'info') {
  // Create notification element
  const notification = document.createElement('div');
  notification.className = `notification notification-${type}`;
  notification.innerHTML = `
        <i class="fas fa-${getNotificationIcon(type)}"></i>
        <span>${message}</span>
    `;

  // Style the notification
  Object.assign(notification.style, {
    position: 'fixed',
    top: '20px',
    right: '20px',
    padding: '15px 20px',
    borderRadius: '10px',
    color: 'white',
    zIndex: '9999',
    display: 'flex',
    alignItems: 'center',
    gap: '10px',
    animation: 'slideInRight 0.3s ease-out',
    backgroundColor: getNotificationColor(type)
  });

  document.body.appendChild(notification);

  // Remove after 3 seconds
  setTimeout(() => {
    notification.style.animation = 'slideOutRight 0.3s ease-in';
    setTimeout(() => {
      if (document.body.contains(notification)) {
        document.body.removeChild(notification);
      }
    }, 300);
  }, 3000);
}

function getNotificationIcon(type) {
  const icons = {
    success: 'check-circle',
    warning: 'exclamation-triangle',
    error: 'times-circle',
    info: 'info-circle'
  };
  return icons[type] || 'info-circle';
}

function getNotificationColor(type) {
  const colors = {
    success: '#28a745',
    warning: '#ffc107',
    error: '#dc3545',
    info: '#17a2b8'
  };
  return colors[type] || '#17a2b8';
}

// Add CSS animations for notifications
const style = document.createElement('style');
style.textContent = `
    @keyframes slideInRight {
        from {
            transform: translateX(100%);
            opacity: 0;
        }
        to {
            transform: translateX(0);
            opacity: 1;
        }
    }
    
    @keyframes slideOutRight {
        from {
            transform: translateX(0);
            opacity: 1;
        }
        to {
            transform: translateX(100%);
            opacity: 0;
        }
    }
`;
document.head.appendChild(style);

// Global functions for button handlers
window.switchTab = switchTab;
window.fillExample = fillExample;
window.shareImage = shareImage;
window.regenerate = regenerate;
window.downloadImage = downloadImage;
